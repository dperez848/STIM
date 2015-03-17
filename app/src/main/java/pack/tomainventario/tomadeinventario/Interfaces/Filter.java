package pack.tomainventario.tomadeinventario.Interfaces;

import pack.tomainventario.tomadeinventario.Models.SBN010D;
import pack.tomainventario.tomadeinventario.Models.SIP517V;

/**
 * Created by tmachado on 14/01/2015.
 */
public interface Filter {
    public void filterSelect(SIP517V sede, SBN010D ubicacion);
}
