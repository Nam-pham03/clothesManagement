package com.example.project_prm.Adapter.User;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.Entities.Cart;
import com.example.project_prm.Entities.CartWithProduct;
import com.example.project_prm.R;
import com.example.project_prm.ViewModel.User.CartViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    public List<CartWithProduct> cartList = new ArrayList<>();
    private OnCartItemClickListener listener;
    private CartViewModel cartViewModel;
    private OnQuantityChangeListener quantityChangeListener;
    private OnItemSelectedListener onItemSelectedListener;
    private Set<CartWithProduct> selectedItems = new HashSet<>();
    public interface OnItemSelectedListener {
        void onSelectionChanged(Set<CartWithProduct> selectedItems);
    }
    public interface OnCartItemClickListener {
        void onDeleteClick(CartWithProduct cart);
    }
    public Set<CartWithProduct> getSelectedItems() {
        return selectedItems;
    }

    public interface OnQuantityChangeListener {
        void onQuantityChanged();
    }

    public CartAdapter(OnCartItemClickListener listener, CartViewModel cartViewModel, OnQuantityChangeListener quantityChangeListener, OnItemSelectedListener onItemSelectedListener) {
        this.listener = listener;
        this.cartViewModel = cartViewModel;
        this.quantityChangeListener = quantityChangeListener;
        this.onItemSelectedListener = onItemSelectedListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartWithProduct cartWithProduct = cartList.get(position);
        holder.bind(cartWithProduct);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public void setCartList(List<CartWithProduct> cartList) {
        this.cartList = cartList;
        notifyDataSetChanged();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName, txtProductQuantity, txtProductPrice;
        ImageView imgDelete;
        CheckBox checkboxSelect;
        ImageButton  btnIncrease, btnDecrease;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductQuantity = itemView.findViewById(R.id.txtProductQuantity);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            checkboxSelect = itemView.findViewById(R.id.checkboxSelect);
        }

        void bind(CartWithProduct cartWithProduct) {
            Cart cart = cartWithProduct.getCart();
            txtProductName.setText(cartWithProduct.getProductName());
            txtProductQuantity.setText("Quantity: " + cart.getQuantity());
            txtProductPrice.setText(String.format("Price: %,.0f VND", cart.getPrice()));

            btnIncrease.setOnClickListener(v -> {
                int currentQuantity = cart.getQuantity();
                int maxStock = cartWithProduct.getProductStock(); // Lấy số lượng sản phẩm tồn kho

                if (currentQuantity < maxStock) {
                    cart.setQuantity(currentQuantity + 1);
                    cartViewModel.update(cart);
                    notifyItemChanged(getAdapterPosition());
                    if (quantityChangeListener != null) {
                        quantityChangeListener.onQuantityChanged(); // Cập nhật tổng giá
                    }
                } else {
                    Toast.makeText(itemView.getContext(), "Max stock!", Toast.LENGTH_SHORT).show();
                }
            });


            btnDecrease.setOnClickListener(v -> {
                if (cart.getQuantity() > 1) {
                    cart.setQuantity(cart.getQuantity() - 1);
                    cartViewModel.update(cart);
                } else {
                    cartViewModel.delete(cart);
                    cartList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                }
                notifyItemChanged(getAdapterPosition());
                if (quantityChangeListener != null) {
                    quantityChangeListener.onQuantityChanged(); // Cập nhật tổng giá
                }
            });

            imgDelete.setOnClickListener(v -> {
                new androidx.appcompat.app.AlertDialog.Builder(itemView.getContext())
                        .setTitle("Warning")
                        .setMessage("Are you sure to delete this from cart?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            listener.onDeleteClick(cartWithProduct);
                            if (quantityChangeListener != null) {
                                quantityChangeListener.onQuantityChanged(); // Cập nhật tổng giá
                            }

                        })
                        .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                        .show();
            });
            checkboxSelect.setOnCheckedChangeListener(null);
            checkboxSelect.setChecked(selectedItems.contains(cartWithProduct));
            checkboxSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedItems.add(cartWithProduct);
                } else {
                    selectedItems.remove(cartWithProduct);
                }
                if (onItemSelectedListener != null) {
                    onItemSelectedListener.onSelectionChanged(selectedItems);
                }
            });


        }
    }

}
