package com.se_p2.hungerbellshipper.ui.home;

import android.text.TextUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.se_p2.hungerbellshipper.common.Common;
import com.se_p2.hungerbellshipper.model.ShippingOrderModel;
import com.se_p2.hungerbellshipper.callback.IShippingOrderCallbackListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel implements IShippingOrderCallbackListener {

    private final MutableLiveData<List<ShippingOrderModel>> shippingOrderMutableData;
    private final MutableLiveData<String> messageError;

    private final IShippingOrderCallbackListener listener;

    public HomeViewModel() {
        shippingOrderMutableData=new MutableLiveData<>();
        messageError=new MutableLiveData<>();
        listener=this;
    }

    public MutableLiveData<String> getMessageError() {
        return messageError;
    }

    public MutableLiveData<List<ShippingOrderModel>> getShippingOrderMutableData(String shipperPhone) {
        if(shipperPhone !=null && !TextUtils.isEmpty(shipperPhone))
            loadOrderByShipper(shipperPhone);
        return shippingOrderMutableData;
    }

    private void loadOrderByShipper(String shipperPhone) {
        List<ShippingOrderModel> tempList=new ArrayList<>();
        Query orderRef= FirebaseDatabase.getInstance().getReference(Common.SHIPPING_ORDER_REF)
                .orderByChild("shipperPhone")
                .equalTo(Common.currentShipperUser.getPhone());
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot orderSnapshot:snapshot.getChildren()){
                    ShippingOrderModel shippingOrderModel=orderSnapshot.getValue(ShippingOrderModel.class);
                    shippingOrderModel.setKey(orderSnapshot.getKey());
                    tempList.add(shippingOrderModel);
                }
                listener.onShippingOrderLoadSuccess(tempList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onShippingOrderLoadFailed(error.toString());

            }
        });
    }

    @Override
    public void onShippingOrderLoadSuccess(List<ShippingOrderModel> shippingOrderModelList) {
        shippingOrderMutableData.setValue(shippingOrderModelList);
    }

    @Override
    public void onShippingOrderLoadFailed(String message) {
        messageError.setValue(message);
    }
}