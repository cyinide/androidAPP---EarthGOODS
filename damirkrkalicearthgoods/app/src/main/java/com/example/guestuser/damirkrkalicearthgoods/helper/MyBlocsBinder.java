package com.example.guestuser.damirkrkalicearthgoods.helper;


import com.example.guestuser.damirkrkalicearthgoods.data.BlocVM;
import com.example.guestuser.damirkrkalicearthgoods.data.Blocs;

import java.util.ArrayList;
import java.util.List;

public class MyBlocsBinder {

    public static List<BlocVM> blocs = null;

    public static List<String> getStringListBlokovi(Blocs blokoviResponse) {
        List<String> result = new ArrayList<>();
        blocs = blokoviResponse.blocs;
        for (BlocVM blok: blokoviResponse.blocs) {
            result.add(blok.getNaziv() + ", " + blok.getOpstina().getnaziv());
        }
        return result;
    }

    public static List<BlocVM> getBlocs() {
        return blocs;
    }
}
