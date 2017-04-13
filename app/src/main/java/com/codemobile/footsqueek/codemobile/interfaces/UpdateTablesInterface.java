package com.codemobile.footsqueek.codemobile.interfaces;

/**
 * Created by greg on 28/03/2017.
 */

public interface UpdateTablesInterface {

    void onComplete();
    void onError();
    void onUpdateNeeded(boolean update);

}
