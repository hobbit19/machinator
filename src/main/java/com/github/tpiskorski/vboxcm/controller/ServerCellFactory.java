package com.github.tpiskorski.vboxcm.controller;

import com.github.tpiskorski.vboxcm.domain.Server;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class ServerCellFactory implements Callback<ListView<Server>, ListCell<Server>> {
    @Override
    public ListCell<Server> call(ListView<Server> param) {
        return new ListCell<>() {

            @Override
            protected void updateItem(Server server, boolean bln) {
                super.updateItem(server, bln);
                if (server != null) {
                    setText(server.getAddress());
                }
            }
        };
    }
}