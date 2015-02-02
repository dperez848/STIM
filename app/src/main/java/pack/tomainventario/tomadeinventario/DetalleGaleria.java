package pack.tomainventario.tomadeinventario;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
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


public class DetalleGaleria extends Activity implements IGaleria{

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

        // ------------------- Obetiendo informacion de BN en Galeria
        Bundle bundle = getIntent().getExtras();
        numeroBn = bundle.getInt("numero");
        // ------------------- Obetiendo informacion de BN en Galeria
        gridView = (GridView) findViewById(R.id.gridViewDetalle);
        aviso = (TextView) findViewById(R.id.d_aviso);
        data = SBN054D.getBn(numeroBn);
        customGridAdapter = new GridViewAdapter(DetalleGaleria.this, R.layout.row_grid, data);
        gridView.setAdapter(customGridAdapter);
        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        gridView.setMultiChoiceModeListener(new GridView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode,int position, long id, boolean checked) {
                final int checkedCount = gridView.getCheckedItemCount();
                switch (checkedCount) {
                    case 0:
                        mode.setSubtitle(null);
                        break;
                    case 1:
                        mode.setSubtitle("1 seleccionado");
                        break;
                    default:
                        mode.setSubtitle("" + checkedCount + " seleccionados");
                        break;
                }
                customGridAdapter.toggleSelection(position);

            }
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                SBN054D selecteditem;
                Log.e("aaaa","hizo touch largo");
                switch (item.getItemId()) {
                    case R.id.delete:
                        SparseBooleanArray selected = customGridAdapter.getSelectedIds();
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                selecteditem = (SBN054D) customGridAdapter.getItem(selected.keyAt(i));
                                SBN054D delete=SBN054D.getSpecificFoto(selecteditem.numeroBn, selecteditem.imagen);
                                delete.delete();
                            }
                        }
                        data.clear();
                        data= SBN054D.getAll();
                        customGridAdapter.updateAdapter(data);
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.menu_multiple_selection, menu);
                return true;
            }
            @Override
            public void onDestroyActionMode(ActionMode mode) {
                customGridAdapter.removeSelection();
            }
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }
        });

        if(!(SBN054D.isEmpty(numeroBn))){
            gridView.setVisibility(View.GONE);
        }
        else {
            aviso.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalle_galeria, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
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
    public void detalle(int numero, int posicion) {
        intent = new Intent(DetalleGaleria.this, VisorFotos.class);
        intent.putExtra("pos",posicion);
        intent.putExtra("numeroBn",numeroBn);
        startActivity(intent);
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
}

