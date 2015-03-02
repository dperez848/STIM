package pack.tomainventario.tomadeinventario;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pack.tomainventario.tomadeinventario.Adapters.GridViewAdapter;
import pack.tomainventario.tomadeinventario.Config.MyFileContentProvider;
import pack.tomainventario.tomadeinventario.DataBase.SBN054D;
import pack.tomainventario.tomadeinventario.Interfaces.IGaleria;


public class DetalleGaleria extends Activity implements IGaleria {

    private ImageView imgFavorite;
    private ProgressDialog mDialog;
    private Intent intent;
    private LongOperation run;
    private List<SBN054D> data = new ArrayList<SBN054D>();
    private Bitmap rotatedBitmap;
    private BitmapFactory.Options options;
    private SBN054D nuevaFoto;
    private PackageManager pm;
    private File out;
    private TextView aviso;
    private int numeroBn;
    private GridView gridView;
    private GridViewAdapter customGridAdapter;
    private final int CAMERA_RESULT = 1;
    private ActionBar actionBar;
    private final CharSequence[] items = { "Deshacer"};
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_galeria);

        // ------------------- Configuracion - inicio
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        actionBar.setTitle("Detalle");
        // ------------------- Configuracion - fin
        prefs = getSharedPreferences("invPreferences", Context.MODE_PRIVATE);
        // ------------------- Obetiendo informacion de BN en Galeria
        Bundle bundle = getIntent().getExtras();
        numeroBn = bundle.getInt("numero");
        // ------------------- Obetiendo informacion de BN en Galeria
        gridView = (GridView) findViewById(R.id.gridViewDetalle);
        aviso = (TextView) findViewById(R.id.d_aviso);
        data = SBN054D.getBn(numeroBn);
        customGridAdapter = new GridViewAdapter(DetalleGaleria.this, R.layout.row_grid, data, prefs.getInt("Login",0));
        gridView.setAdapter(customGridAdapter);

        if(!(SBN054D.isEmpty(numeroBn))) gridView.setVisibility(View.GONE);
        else aviso.setVisibility(View.GONE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalle_galeria, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(prefs.getInt("Login",0)!=2){
            menu.findItem(R.id.action_add).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                Intent intent1 = new Intent();
                setResult(RESULT_OK, intent1);
                finish();
                return true;
            case R.id.action_add:
                pm = getPackageManager();
                if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    i.putExtra(MediaStore.EXTRA_OUTPUT, MyFileContentProvider.CONTENT_URI);
                    startActivityForResult(i, CAMERA_RESULT);
                } else {
                    Toast.makeText(getBaseContext(), "La camara no está disponible", Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CAMERA_RESULT) {
            out = new File(getFilesDir(), "newImage.jpg");
            if(!out.exists()) {
                Toast.makeText(getBaseContext(),
                        "Error while capturing image", Toast.LENGTH_LONG)
                        .show();
                return;
            }
            mDialog = new ProgressDialog(this);
            run = new LongOperation();
            run.execute();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        imgFavorite = null;
    }
    public static int getImageOrientation(String imagePath){
        int rotate = 0;
        try {

            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(
                    imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotate;
    }

    private Bitmap resizeBitmap(Bitmap bitmap, int maxWidthHeight) {
        int[] widthHeight = getWidthHeight(bitmap.getWidth(), bitmap.getHeight(), maxWidthHeight);
        bitmap = Bitmap.createScaledBitmap(bitmap, widthHeight[0], widthHeight[1], true);
        return bitmap;
    }

    private int[] getWidthHeight(int width, int height, int maxWidthHeight) {
        int[] result = new int[2];
        float ratio;

        if (width > height) {
            ratio = (float) width / height;
            result[0] = maxWidthHeight;
            result[1] = Math.round(maxWidthHeight / ratio);
        } else if (width < height) {
            ratio = (float) height / width;
            result[0] = Math.round(maxWidthHeight / ratio);
            result[1] = maxWidthHeight;
        } else {
            result[0] = maxWidthHeight;
            result[1] = maxWidthHeight;
        }
        return result;
    }

    @Override
    public void detalle(int numero, int position) {
        intent = new Intent(DetalleGaleria.this, VisorFotos.class);
        intent.putExtra("pos",position);
        intent.putExtra("numeroBn",numeroBn);
        startActivity(intent);
    }

    @Override
    public void onLong(int numero, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetalleGaleria.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                SBN054D selecteditem;
                List<SBN054D> info;
                selecteditem = (SBN054D) customGridAdapter.getItem(position);
                SBN054D delete = SBN054D.getSpecificFoto(selecteditem.numeroBn, selecteditem.imagen);
                delete.delete();
                info = SBN054D.getBn(selecteditem.numeroBn);
                customGridAdapter.updateAdapter(info);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void dataSet() {
        aviso.setVisibility(View.GONE);
        gridView.setVisibility(View.VISIBLE);
        customGridAdapter.addFoto(nuevaFoto);
        customGridAdapter.notifyDataSetChanged();
    }

    private class LongOperation extends AsyncTask<Void, Void, Void> {
        private ByteArrayOutputStream arrayByte;
        private String encoded;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.setTitle("Cargando");
            mDialog.setMessage("Espere mientras la imagen se carga");
            mDialog.setIndeterminate(true);
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            Matrix matrix= new Matrix();
            options = new BitmapFactory.Options();
            options.inSampleSize = 6;
            matrix.postRotate(getImageOrientation(out.getAbsolutePath()));

            rotatedBitmap = resizeBitmap(Bitmap.createBitmap(BitmapFactory.decodeFile(out.getAbsolutePath(),options),
                    0, 0, BitmapFactory.decodeFile(out.getAbsolutePath(),options).getWidth(),
                    BitmapFactory.decodeFile(out.getAbsolutePath(),options).getHeight(), matrix, true),800);
            // - - - Codifica a Base64
            arrayByte = new ByteArrayOutputStream();
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, arrayByte);
            encoded = Base64.encodeToString(arrayByte.toByteArray(), Base64.DEFAULT);

            nuevaFoto = new SBN054D();
            nuevaFoto.idInventario = 1;
            nuevaFoto.idInventarioActivo = 1;
            nuevaFoto.numeroBn =numeroBn;
            nuevaFoto.imagen = encoded;
            nuevaFoto.nombre =""+numeroBn;
            nuevaFoto.save();

            return null;
        }

        @Override
        protected void onPostExecute(Void results) {
            dataSet();
            mDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent();
        setResult(RESULT_OK, intent1);
        finish();
    }
}

