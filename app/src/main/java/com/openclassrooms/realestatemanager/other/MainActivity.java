package com.openclassrooms.realestatemanager.other;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private TextView textViewMain;
    private TextView textViewQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.textViewMain = findViewById(R.id.activity_main_activity_text_view_main);
        this.textViewQuantity = findViewById(R.id.activity_main_activity_text_view_quantity);

        this.configureTextViewMain();
        this.configureTextViewQuantity();
    }

    private void configureTextViewMain(){
        this.textViewMain.setTextSize(15);
        this.textViewMain.setText("Le premier bien immobilier enregistré vaut ");
    }


    private void configureTextViewQuantity(){
        float quantity = Utils.convertDollarToEuro(100); // Problème : Résultat de conversion stocké dans un int au lieu d'un flottant.
        this.textViewQuantity.setTextSize(20);
        this.textViewQuantity.setText(quantity + "" + "$"); // Problème : Affichage de la quantité sans indication de devise.
    }
}
