package com.se_p2.hungerbellshipper.callback;

import com.se_p2.hungerbellshipper.model.ShippingOrderModel;

import java.util.List;

public interface IShippingOrderCallbackListener {
    void onShippingOrderLoadSuccess(List<ShippingOrderModel> shippingOrderModelList);
    void onShippingOrderLoadFailed(String message);
}
