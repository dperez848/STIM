package pack.tomainventario.tomadeinventario.Interfaces;

import pack.tomainventario.tomadeinventario.DataBase.SBN203D;
import pack.tomainventario.tomadeinventario.DataBase.SIP517V;

/**
 * Created by tmachado on 14/01/2015.
 */
public interface Filter {
    public void filterSelect(SIP517V sede, SBN203D status);
}
