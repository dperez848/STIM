package pack.tomainventario.tomadeinventario.Interfaces;

import pack.tomainventario.tomadeinventario.Models.SIP501V;

/**
 * Created by tmachado on 26/01/2015.
 */
public interface Rpu {
    public void openRpuDialog(int bn);
    public void onRpuItemClick(SIP501V rpu);
}
