package ui;

import model.CartItem;
import model.Pizza;
import model.ShoppingCart;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CartPanel extends JPanel {
    final private ShoppingCart cart = new ShoppingCart();
    final private CartItemListPanel cartItemListPanel = new CartItemListPanel(cart);
    final private TotalPricePanel totalPricePanel = new TotalPricePanel(cart);
    public CartPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        CartTitlePanel titleLabel = new CartTitlePanel();
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        titleLabel.setBorder(new EmptyBorder(8, 8, 8, 8));

        JButton checkoutButton = new JButton("送出");
        checkoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "已送出\n您的pizza將於30分鐘後抵達\uD83C\uDF55\uD83C\uDF55");
            reset();
        });
        checkoutButton.setPreferredSize(new Dimension(100, 50));

        add(titleLabel, BorderLayout.NORTH);
        add(cartItemListPanel, BorderLayout.CENTER);

        JButton resetButton = new JButton(("重置"));
        resetButton.addActionListener(e -> reset());
        resetButton.setPreferredSize(new Dimension(100, 50));

        JPanel innerPanel = new JPanel(new GridBagLayout());
        innerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER;

        innerPanel.add(checkoutButton, gbc);

        gbc.gridx++;
        innerPanel.add(resetButton, gbc);

        CartDiscountPanel cartDiscountPanel = new CartDiscountPanel(cart, totalPricePanel);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(cartDiscountPanel);
        bottomPanel.add(totalPricePanel);
        bottomPanel.add(innerPanel);
        bottomPanel.add(Box.createHorizontalGlue());

        add(bottomPanel, BorderLayout.SOUTH);
    }
    public void reset(){
        cart.removeAll();
        cartItemListPanel.paintItems();
        totalPricePanel.updateTotalPrice();
    }

    public void addItem(Pizza pizza, String size, int quantity) {
        cart.addCartItem(pizza, size, quantity);
        cartItemListPanel.paintItems();
        totalPricePanel.updateTotalPrice();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 0);
    }
}

class CartDiscountPanel extends JPanel {
    final private JTextField discountField;
    final private JLabel messageLabel;
    final private ShoppingCart cart;
    final private TotalPricePanel totalPricePanel;

    public CartDiscountPanel(ShoppingCart cart, TotalPricePanel totalPricePanel) {
        this.cart = cart;
        this.totalPricePanel = totalPricePanel;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
        inputPanel.setBackground(Color.WHITE);

        discountField = new JTextField(10);
        discountField.setText("輸入折扣碼");
        JButton applyButton = new JButton("輸入");
        messageLabel = new JLabel(" ");

        applyButton.addActionListener(e -> applyDiscount());

        inputPanel.add(discountField);
        inputPanel.add(applyButton);
        add(inputPanel);
        add(messageLabel);
    }

    private void applyDiscount() {
        String code = discountField.getText();
        cart.applyDiscountCode(code);
        if (cart.getDiscountPercentage() > 0) {
            messageLabel.setText("使用成功：只需付" + (int)((1 - cart.getDiscountPercentage()) * 100) + "%");
        } else {
            messageLabel.setText("折扣碼不存在");
        }
        totalPricePanel.updateTotalPrice();
    }
}

class CartTitlePanel extends JPanel {
    public CartTitlePanel() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("購物車");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 22));
        titleLabel.setBorder(new EmptyBorder(8, 8, 8, 8));
        add(Box.createHorizontalGlue());
        add(titleLabel);
        add(Box.createHorizontalGlue());
    }
}

class CartItemListPanel extends JPanel {
    final private ShoppingCart cart;
    public CartItemListPanel(ShoppingCart cart) {
        this.cart = cart;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
    }


    public void paintItems() {
        removeAll();
        for (CartItem item : cart.getItems()) {
            add(new CartItemPanel(item));
        }
        revalidate();
        repaint();
    }
}

class TotalPricePanel extends JPanel {
    final private JLabel totalPriceLabel;
    final private ShoppingCart cart;
    public TotalPricePanel(ShoppingCart cart) {
        this.cart = cart;

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBackground(Color.WHITE);

        totalPriceLabel = new JLabel("合計：$" + cart.calculateTotal());
        totalPriceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        totalPriceLabel.setFont(new Font("SansSerif", Font.PLAIN, 22));
        totalPriceLabel.setBorder(new EmptyBorder(8, 8, 8, 8));

        add(Box.createHorizontalGlue());
        add(totalPriceLabel);
        add(Box.createHorizontalGlue());

    }
    public void updateTotalPrice() {
        totalPriceLabel.setText("合計：$" + cart.calculateTotal());
        revalidate();
        repaint();
    }
}