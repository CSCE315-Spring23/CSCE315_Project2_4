/*
 * Copyright (c) 2011, 2012 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

// Comment this package line out when compiling with command line
//package application;

import java.util.Vector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ServerController {
    @FXML private Button hamburger;
    @FXML private Button baconHamburger;
    @FXML private Button doubleHamburger;
    @FXML private Button cheeseburger;
    @FXML private Button baconCheeseburger;
    @FXML private Button doubleCheeseburger;
    @FXML private Button bbHamburger;
    @FXML private Button baconBBHamburger;
    @FXML private Button doubleBBHamburger;
    @FXML private Button bbCheeseburger;
    @FXML private Button baconBBCheeseburger;
    @FXML private Button doubleBBCheeseburger;
    private Vector<Integer> currOrder = new Vector<Integer>();

    public void initialize(){
        hamburger.setOnAction(e -> addItemToOrder(1));
        baconHamburger.setOnAction(e -> addItemToOrder(5));
        doubleHamburger.setOnAction(e -> addItemToOrder(9));
        cheeseburger.setOnAction(e -> addItemToOrder(currOrder ,2));
        baconCheeseburger.setOnAction(e -> addItemToOrder(currOrder ,6));
        doubleCheeseburger.setOnAction(e -> addItemToOrder(currOrder ,10));
        bbHamburger.setOnAction(e -> addItemToOrder(currOrder ,3));
        baconBBHamburger.setOnAction(e -> addItemToOrder(currOrder ,7));
        doubleBBHamburger.setOnAction(e -> addItemToOrder(currOrder ,11));
        bbCheeseburger.setOnAction(e -> addItemToOrder(currOrder ,4));
        baconBBCheeseburger.setOnAction(e -> addItemToOrder(currOrder ,8));
        doubleBBCheeseburger.setOnAction(e -> addItemToOrder(currOrder ,12));
    }

    private void addItemToOrder(int menuItemId) {
        currOrder.add(menuItemId);
    }

    private void confirmOrder(){
        currOrder = new Vector<Integer>();
    }
}

/*
  * public class placeholderController {
        @FXML private Button hamburger;

        public void initialize(){
            hamburger.setOnAction(e -> addItemToOrder(1));
        }

        //button functions
        
        
        }

    }
  */
