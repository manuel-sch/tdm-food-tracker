package com.example.mealstock.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;

import com.example.mealstock.models.Product;
import com.example.mealstock.viewmodels.ProductDetailViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions;
import com.google.mlkit.nl.languageid.LanguageIdentifier;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.Objects;

public class ToEnglishTranslator {

    private Translator toEnglishTranslator;

    private final AndroidViewModel viewModel;
    private final Product currentProduct;
    private final String TAG;
    private String productInformation;

    public ToEnglishTranslator(AndroidViewModel viewModel, Product currentProduct){
        this.viewModel = viewModel;
        TAG = ToEnglishTranslator.class.getSimpleName();
        this.currentProduct = currentProduct;
    }

    public void setTranslatedToEnglishProductInformationForRecipeSearch() {

        productInformation = "";
        if(!currentProduct.getGenericName().isEmpty())
            productInformation = currentProduct.getGenericName();
        else if (!currentProduct.getProductName().isEmpty())
            productInformation = currentProduct.getProductName();
        Log.d(TAG, "setTranslatedToEnglishProductInformationForRecipeSearch: " + "Product Information to Translate: " + productInformation);

        if (!productInformation.isEmpty()) {
            LanguageIdentifier languageIdentifier =
                    LanguageIdentification.getClient(
                            new LanguageIdentificationOptions.Builder()
                                    .setConfidenceThreshold(0.5f)
                                    .build());
            languageIdentifier.identifyLanguage(productInformation)
                    .addOnSuccessListener(
                            new OnSuccessListener<String>() {
                                @Override
                                public void onSuccess(@Nullable String languageCode) {
                                    // undetermined
                                    if (languageCode.equals("und")) {
                                        Log.d(TAG, "Can't identify language.");
                                        ProductDetailViewModel productDetailViewModel = (ProductDetailViewModel) viewModel;
                                        productDetailViewModel.requestRecipesFromServerWithInformation(currentProduct.getProductName(), true);
                                    } else {
                                        Log.d(TAG, "Languagecode: " + languageCode);
                                        translateGenericNameBasedOnLanguageTag(languageCode);
                                    }
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    ProductDetailViewModel productDetailViewModel = (ProductDetailViewModel) viewModel;
                                    productDetailViewModel.requestRecipesFromServerWithInformation(currentProduct.getProductName(), true);
                                }
                            });
        }
    }

    private void translateGenericNameBasedOnLanguageTag(String languageTag){
        setUpTranslator(languageTag);
        if(toEnglishTranslator != null){
            Log.d(TAG, "translateGenericNameBasedOnLanguageTag: Translating now");
            translate();
        }
        else{
            if(viewModel instanceof ProductDetailViewModel){
                ProductDetailViewModel productDetailViewModel = (ProductDetailViewModel) viewModel;
                productDetailViewModel.requestRecipesFromServerWithInformation(currentProduct.getProductName(), true);
            }
        }
    }

    private void setUpTranslator(String languageTag){
        if (TranslateLanguage.fromLanguageTag(languageTag) != null) {
            Log.d(TAG, "setUpTranslator: with language Tag: " + languageTag);
            TranslatorOptions options =
                    new TranslatorOptions.Builder()
                            .setSourceLanguage((Objects.requireNonNull(TranslateLanguage.fromLanguageTag(languageTag))))
                            .setTargetLanguage(TranslateLanguage.ENGLISH)
                            .build();
            toEnglishTranslator =
                    Translation.getClient(options);
            DownloadConditions conditions = new DownloadConditions.Builder()
                    .requireWifi()
                    .build();
            toEnglishTranslator.downloadModelIfNeeded(conditions)
                    .addOnSuccessListener(
                            new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    Log.d(TAG, "onSuccess: " + "Languagemodel downloaded.");
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Model couldn’t be downloaded or other internal error.
                                    // ...
                                }
                            });
        }

    }

    private void translate(){
        toEnglishTranslator.translate(productInformation)
                .addOnSuccessListener(
                        new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                String translatedGenericName = (String) o;
                                Log.d(TAG, "onSuccess: Übersetzter Name des Produktes: " + translatedGenericName);
                                if(viewModel instanceof ProductDetailViewModel){
                                    ProductDetailViewModel productDetailViewModel = (ProductDetailViewModel) viewModel;
                                    productDetailViewModel.requestRecipesFromServerWithInformation(translatedGenericName, false);
                                }
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Error.
                                // ...
                            }
                        });
    }

    public void close(){
        if(toEnglishTranslator != null)
            toEnglishTranslator.close();
    }
}
