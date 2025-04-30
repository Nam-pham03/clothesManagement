package com.example.project_prm.Activity.User.ChatBot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.Activity.User.Shop.ProductListActivity;
import com.example.project_prm.Adapter.User.ChatAdapter;
import com.example.project_prm.Entities.Product;
import com.example.project_prm.R;
import com.example.project_prm.Repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatbotActivity extends AppCompatActivity {

    private EditText chatInput;
    private Button sendChatBtn;
    private RecyclerView chatbox;
    private ChatAdapter chatAdapter;
    private List<String> chatMessages = new ArrayList<>();
    private ProductRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot); // Link to activity_chatbot.xml

        // Referencing the views from the activity_chatbot.xml layout
        chatInput = findViewById(R.id.chatInput);
        sendChatBtn = findViewById(R.id.sendChatBtn);
        chatbox = findViewById(R.id.chatbox);

        // Set up RecyclerView
        chatAdapter = new ChatAdapter(this, chatMessages);
        chatbox.setLayoutManager(new LinearLayoutManager(this));
        chatbox.setAdapter(chatAdapter);

        sendChatBtn.setOnClickListener(v -> handleChat());
    }

    private void handleChat() {
        String userMessage = chatInput.getText().toString().trim();
        if (userMessage.isEmpty()) return;

        chatInput.setText("");
        chatMessages.add("You: " + userMessage);
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);

        GeminiApi geminiApi = GeminiRetrofitClient.getInstance().create(GeminiApi.class);

        // Danh sách sản phẩm cố định
        List<Product> fixedProducts = new ArrayList<>();
        fixedProducts.add(new Product("Áo thun nam", 1, "Nike", "A001", 50, "Cái", 150000, 10, 120000, "Nike Corp", "image1.jpg", "2025-03-28", "2025-03-28", 0));
        fixedProducts.add(new Product("Áo sơ mi nữ", 2, "Adidas", "A002", 30, "Cái", 200000, 15, 170000, "Adidas Ltd", "image2.jpg", "2025-03-28", "2025-03-28", 0));
        fixedProducts.add(new Product("Áo hoodie nam", 3, "Puma", "A003", 20, "Cái", 180000, 5, 170000, "Puma Inc", "image3.jpg", "2025-03-28", "2025-03-28", 0));

        // Chuỗi thông tin sản phẩm
        StringBuilder productInfo = new StringBuilder("Danh sách sản phẩm có sẵn:\n");
        for (Product product : fixedProducts) {
            productInfo.append(String.format("- %s: %,.0f VND (Giảm %d%%)\n", product.getName(), product.getSale_price(), (int) product.getDiscount()));
        }

        // Format the message with the user's input
        String templateMessage = "Bạn là nhân viên tư vấn bán quần áo của thương hiệu Kibyhunter Clothes và đưa ra tư vấn ngắn gọn chính xác, sử dụng ngôn ngữ con người. Dưới đây là danh sách sản phẩm có sẵn,gợi ý sản phẩm tên, giá, chất thiệu kiểu dáng, có được giảm giá hay không :\n%s\n\nCâu hỏi của khách hàng: %s";
        String formattedMessage = String.format(templateMessage, productInfo.toString(), userMessage);

        // Create GeminiRequest with formatted message
        List<GeminiRequest.Content> contents = new ArrayList<>();
        List<GeminiRequest.Part> parts = new ArrayList<>();
        parts.add(new GeminiRequest.Part(formattedMessage));  // Use formatted message here
        contents.add(new GeminiRequest.Content("user", parts));

        GeminiRequest request = new GeminiRequest(contents);

        // Make the API call
        geminiApi.generateResponse(request).enqueue(new Callback<GeminiResponse>() {
            @Override
            public void onResponse(Call<GeminiResponse> call, Response<GeminiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String botMessage = response.body().getCandidates().get(0).getContent().getParts().get(0).getText();
                    chatMessages.add("Bot: " + botMessage);
                    chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                    chatbox.scrollToPosition(chatMessages.size() - 1);
                }
            }

            @Override
            public void onFailure(Call<GeminiResponse> call, Throwable t) {
                chatMessages.add("Bot: Error occurred");
                chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                Toast.makeText(ChatbotActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Function to go back to MainActivity after the chat
    public void goToMainActivity(View view) {
        Intent intent = new Intent(ChatbotActivity.this, ProductListActivity.class);
        startActivity(intent);
        finish();
    }
}
