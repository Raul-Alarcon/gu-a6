package com.example.loginproject.UI.VIewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.loginproject.Models.GastosDia;
import com.example.loginproject.Repositories.DailyExpensesRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DailyExpensesVM extends ViewModel {
    private MutableLiveData<List<GastosDia>> expenseLiveData;
    private DailyExpensesRepository repository;
    public DailyExpensesVM(){
        expenseLiveData = new MutableLiveData<>();
        repository = new DailyExpensesRepository();
    }
    public void addDailyExpense(GastosDia mainObject, OnSuccessListener<DocumentReference> onSuccess, OnFailureListener
            onFailure) {
        repository.addDailyExpense(mainObject, onSuccess, onFailure);
    }
    public void finalizeBudget(String budgetId, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        repository.endBudget(budgetId, onSuccess, onFailure);
    }
    public LiveData<List<GastosDia>> getDailyExpensesLiveData() {
        return expenseLiveData;
    }
    public void listenForExpensesChanges(String presupuestoId) {
        repository.listenForExpensesChanges(presupuestoId, (querySnapshot, e) -> {
            if (e != null) {
                return;
            }
            List<GastosDia> gastosDias = new ArrayList<>();
            for (QueryDocumentSnapshot document : querySnapshot) {
                GastosDia mainObject = document.toObject(GastosDia.class);
                gastosDias.add(mainObject);
            }
            expenseLiveData.postValue(gastosDias);
        });
    }

}
