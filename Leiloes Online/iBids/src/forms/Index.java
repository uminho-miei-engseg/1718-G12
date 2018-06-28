package forms;

import auctions.AuctionClient;
import auctions.Client;
import auctions.Crypto;
import java.awt.Color;
import java.net.URL;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class Index extends javax.swing.JFrame {

    private Client cli = new Client();
    private PublicKey pub, serverPub;
    private String pubString;
    private String pubStringHash;
    private PrivateKey pk;
    // flag que indica se a lista auctions está preenchida;
    private int updateflag = 0;
    String [] auctionKeys;
    private int auction_flag = 0;
    private int claim_flag = 0;
    private int all_flag = 0;
    private JScrollPane auct = new JScrollPane();
    private JScrollPane claimm = new JScrollPane();
    private JScrollPane allauct = new JScrollPane();

    public Index() throws KeyStoreException, NoSuchAlgorithmException, CertificateException {

        URL iconURL = getClass().getResource("/images/logo.jpg");
        ImageIcon icon = new ImageIcon(iconURL);
        this.setIconImage(icon.getImage());

        

        cli.init();
        pub = cli.getPub();
        pk = cli.getPk();
        
        pubString = Crypto.getStringFromKey(pub);
        pubStringHash = Crypto.hash(pubString, "SHA-256");

        serverPub = cli.getSpub();

        String result = cli.register(pub, pk, serverPub);
        if (result == null) {
            JOptionPane.showMessageDialog(null, "User successfully registered!");
        } else {
            JOptionPane.showMessageDialog(null, result);
            System.exit(0);
        }
        // Inicia as componentes da interface
        initComponents();
        home_content.setVisible(true);
        create_content.setVisible(false);
        bid_content.setVisible(false);
        claim_content.setVisible(false);
        all_auction_content.setVisible(false);
        
        user_pub_key_label.setText("Pub key: " + Crypto.hash(Crypto.getStringFromKey(pub),"SHA-256"));
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sidepane = new javax.swing.JPanel();
        logo = new javax.swing.JLabel();
        home = new javax.swing.JPanel();
        home_icon = new javax.swing.JLabel();
        home_label = new javax.swing.JLabel();
        create = new javax.swing.JPanel();
        create_icon = new javax.swing.JLabel();
        create_label = new javax.swing.JLabel();
        all_auctions = new javax.swing.JPanel();
        all_auctions_icon = new javax.swing.JLabel();
        myauctions_label1 = new javax.swing.JLabel();
        bid = new javax.swing.JPanel();
        bid_icon = new javax.swing.JLabel();
        bid_label = new javax.swing.JLabel();
        claim = new javax.swing.JPanel();
        claim_icon = new javax.swing.JLabel();
        claim_label = new javax.swing.JLabel();
        user_pub_key_label = new javax.swing.JLabel();
        home_content = new javax.swing.JPanel();
        home_title = new javax.swing.JLabel();
        create_content = new javax.swing.JPanel();
        create_title = new javax.swing.JLabel();
        jTextFieldDesc = new javax.swing.JTextField();
        desc_icon = new javax.swing.JLabel();
        desc_label = new javax.swing.JLabel();
        jTextFieldBaseprice = new javax.swing.JTextField();
        base_price_label = new javax.swing.JLabel();
        base_price_icon = new javax.swing.JLabel();
        jTextFieldTime = new javax.swing.JTextField();
        duration_label = new javax.swing.JLabel();
        duration_icon = new javax.swing.JLabel();
        create_button = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        bid_content = new javax.swing.JPanel();
        bid_title = new javax.swing.JLabel();
        auction_to_bid_label = new javax.swing.JLabel();
        auction_to_bid_icon = new javax.swing.JLabel();
        bid_button = new javax.swing.JButton();
        jTextFieldPrice = new javax.swing.JTextField();
        price_label = new javax.swing.JLabel();
        price_icon = new javax.swing.JLabel();
        update_auction_list_button = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        claim_content = new javax.swing.JPanel();
        claim_title = new javax.swing.JLabel();
        auction_to_bid_label1 = new javax.swing.JLabel();
        auction_to_bid_icon1 = new javax.swing.JLabel();
        bid_button1 = new javax.swing.JButton();
        update_auction_list_button1 = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox<>();
        all_auction_content = new javax.swing.JPanel();
        update_allauctions_button = new javax.swing.JButton();
        all_auctions_title = new javax.swing.JLabel();
        mine_button = new javax.swing.JButton();
        active_button = new javax.swing.JButton();
        won_active = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("iBids");
        setBackground(new java.awt.Color(255, 255, 255));
        setLocationByPlatform(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        sidepane.setBackground(new java.awt.Color(255, 255, 255));
        sidepane.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logo.jpg"))); // NOI18N
        logo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        sidepane.add(logo, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 0, -1, -1));

        home.setBackground(new java.awt.Color(255, 255, 255));
        home.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                homeMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                homeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                homeMouseExited(evt);
            }
        });
        home.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        home_icon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        home_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_Home_15px.png"))); // NOI18N
        home.add(home_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 14, 30, 30));

        home_label.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        home_label.setForeground(new java.awt.Color(67, 67, 67));
        home_label.setText("Home");
        home.add(home_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 14, 107, 30));

        sidepane.add(home, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 320, 50));

        create.setBackground(new java.awt.Color(255, 255, 255));
        create.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                createMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                createMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                createMouseExited(evt);
            }
        });
        create.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        create_icon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        create_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_Plus_15px_3.png"))); // NOI18N
        create.add(create_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 30, 30));

        create_label.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        create_label.setForeground(new java.awt.Color(67, 67, 67));
        create_label.setText("Create Auction");
        create.add(create_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 11, 107, 30));

        sidepane.add(create, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, 320, 50));

        all_auctions.setBackground(new java.awt.Color(255, 255, 255));
        all_auctions.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                all_auctionsMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                all_auctionsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                all_auctionsMouseExited(evt);
            }
        });
        all_auctions.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        all_auctions_icon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        all_auctions_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_Check_All_15px_1.png"))); // NOI18N
        all_auctions.add(all_auctions_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 30, 30));

        myauctions_label1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        myauctions_label1.setForeground(new java.awt.Color(67, 67, 67));
        myauctions_label1.setText("Check All Auctions");
        all_auctions.add(myauctions_label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 11, 130, 30));

        sidepane.add(all_auctions, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 320, 50));

        bid.setBackground(new java.awt.Color(255, 255, 255));
        bid.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bidMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bidMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bidMouseExited(evt);
            }
        });
        bid.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        bid_icon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bid_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_Auction_15px.png"))); // NOI18N
        bid.add(bid_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 30, 30));

        bid_label.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        bid_label.setForeground(new java.awt.Color(67, 67, 67));
        bid_label.setText("Bid");
        bid.add(bid_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 11, 107, 30));

        sidepane.add(bid, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 320, 50));

        claim.setBackground(new java.awt.Color(255, 255, 255));
        claim.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                claimMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                claimMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                claimMouseExited(evt);
            }
        });
        claim.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        claim_icon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        claim_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_Redeem_15px.png"))); // NOI18N
        claim.add(claim_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 30, 30));

        claim_label.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        claim_label.setForeground(new java.awt.Color(67, 67, 67));
        claim_label.setText("Claim");
        claim.add(claim_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 11, 107, 30));

        sidepane.add(claim, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, 320, 50));

        user_pub_key_label.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        user_pub_key_label.setForeground(new java.awt.Color(67, 67, 67));
        sidepane.add(user_pub_key_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 500, 370, 20));

        getContentPane().add(sidepane, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 340, 530));

        home_content.setBackground(new java.awt.Color(67, 67, 67));
        home_content.setPreferredSize(new java.awt.Dimension(550, 530));
        home_content.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        home_title.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        home_title.setForeground(new java.awt.Color(255, 255, 255));
        home_title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        home_title.setText("Welcome to iBids");
        home_title.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        home_content.add(home_title, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 240, 300, 40));

        getContentPane().add(home_content, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 0, 550, 530));

        create_content.setBackground(new java.awt.Color(67, 67, 67));
        create_content.setPreferredSize(new java.awt.Dimension(550, 530));
        create_content.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        create_title.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        create_title.setForeground(new java.awt.Color(255, 255, 255));
        create_title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        create_title.setText("Add new auction");
        create_title.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        create_content.add(create_title, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, 200, 40));

        jTextFieldDesc.setBackground(new java.awt.Color(67, 67, 67));
        jTextFieldDesc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextFieldDesc.setForeground(new java.awt.Color(255, 255, 255));
        jTextFieldDesc.setToolTipText("Item description");
        jTextFieldDesc.setBorder(null);
        jTextFieldDesc.setMargin(new java.awt.Insets(2, 20, 2, 2));
        create_content.add(jTextFieldDesc, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 230, 220, 40));

        desc_icon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        desc_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_Create_20px.png"))); // NOI18N
        create_content.add(desc_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 230, 40, 40));

        desc_label.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        desc_label.setForeground(new java.awt.Color(255, 255, 255));
        desc_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        desc_label.setText("Description");
        create_content.add(desc_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 230, 110, 40));

        jTextFieldBaseprice.setBackground(new java.awt.Color(67, 67, 67));
        jTextFieldBaseprice.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextFieldBaseprice.setForeground(new java.awt.Color(255, 255, 255));
        jTextFieldBaseprice.setToolTipText("Item base price");
        jTextFieldBaseprice.setBorder(null);
        create_content.add(jTextFieldBaseprice, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 290, 220, 40));

        base_price_label.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        base_price_label.setForeground(new java.awt.Color(255, 255, 255));
        base_price_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        base_price_label.setText("Base Price");
        create_content.add(base_price_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 290, 110, 40));

        base_price_icon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        base_price_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_Euro_30px_1.png"))); // NOI18N
        create_content.add(base_price_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 290, 40, 40));

        jTextFieldTime.setBackground(new java.awt.Color(67, 67, 67));
        jTextFieldTime.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextFieldTime.setForeground(new java.awt.Color(255, 255, 255));
        jTextFieldTime.setToolTipText("Auction duration");
        jTextFieldTime.setBorder(null);
        jTextFieldTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldTimeActionPerformed(evt);
            }
        });
        create_content.add(jTextFieldTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 350, 220, 40));

        duration_label.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        duration_label.setForeground(new java.awt.Color(255, 255, 255));
        duration_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        duration_label.setText("Duration");
        create_content.add(duration_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 350, 110, 40));

        duration_icon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        duration_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_Time_20px.png"))); // NOI18N
        create_content.add(duration_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 350, 40, 40));

        create_button.setBackground(new java.awt.Color(255, 255, 255));
        create_button.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        create_button.setForeground(new java.awt.Color(67, 67, 67));
        create_button.setText("Create");
        create_button.setToolTipText("");
        create_button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        create_button.setName(""); // NOI18N
        create_button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                create_buttonMouseClicked(evt);
            }
        });
        create_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                create_buttonActionPerformed(evt);
            }
        });
        create_content.add(create_button, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 420, -1, -1));

        jSeparator1.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator1.setOpaque(true);
        jSeparator1.setRequestFocusEnabled(false);
        jSeparator1.setVerifyInputWhenFocusTarget(false);
        create_content.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 390, 220, 2));

        jSeparator2.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator2.setOpaque(true);
        jSeparator2.setRequestFocusEnabled(false);
        jSeparator2.setVerifyInputWhenFocusTarget(false);
        create_content.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 270, 220, -1));

        jSeparator3.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator3.setOpaque(true);
        jSeparator3.setRequestFocusEnabled(false);
        jSeparator3.setVerifyInputWhenFocusTarget(false);
        create_content.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 330, 220, 2));

        getContentPane().add(create_content, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 0, 550, 530));

        bid_content.setBackground(new java.awt.Color(67, 67, 67));
        bid_content.setPreferredSize(new java.awt.Dimension(550, 530));
        bid_content.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        bid_title.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        bid_title.setForeground(new java.awt.Color(255, 255, 255));
        bid_title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bid_title.setText("Auction bid");
        bid_title.setToolTipText("");
        bid_title.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bid_content.add(bid_title, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, 140, 40));

        auction_to_bid_label.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        auction_to_bid_label.setForeground(new java.awt.Color(255, 255, 255));
        auction_to_bid_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        auction_to_bid_label.setText("Auction to bid");
        bid_content.add(auction_to_bid_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 380, 150, 40));

        auction_to_bid_icon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        auction_to_bid_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_Auction_20px.png"))); // NOI18N
        bid_content.add(auction_to_bid_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 380, 40, 40));

        bid_button.setBackground(new java.awt.Color(255, 255, 255));
        bid_button.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        bid_button.setForeground(new java.awt.Color(67, 67, 67));
        bid_button.setText("Bid");
        bid_button.setBorderPainted(false);
        bid_button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bid_button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bid_buttonMouseClicked(evt);
            }
        });
        bid_content.add(bid_button, new org.netbeans.lib.awtextra.AbsoluteConstraints(423, 480, -1, -1));

        jTextFieldPrice.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextFieldPrice.setForeground(new java.awt.Color(67, 67, 67));
        jTextFieldPrice.setToolTipText("");
        jTextFieldPrice.setBorder(null);
        jTextFieldPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldPriceActionPerformed(evt);
            }
        });
        bid_content.add(jTextFieldPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 430, 220, 40));

        price_label.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        price_label.setForeground(new java.awt.Color(255, 255, 255));
        price_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        price_label.setText("Price");
        bid_content.add(price_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 430, 150, 40));

        price_icon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        price_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_Euro_30px_1.png"))); // NOI18N
        bid_content.add(price_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 430, 40, 40));

        update_auction_list_button.setBackground(new java.awt.Color(255, 255, 255));
        update_auction_list_button.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        update_auction_list_button.setForeground(new java.awt.Color(67, 67, 67));
        update_auction_list_button.setText("Update list");
        update_auction_list_button.setBorderPainted(false);
        update_auction_list_button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        update_auction_list_button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                update_auction_list_buttonMouseClicked(evt);
            }
        });
        update_auction_list_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                update_auction_list_buttonActionPerformed(evt);
            }
        });
        bid_content.add(update_auction_list_button, new org.netbeans.lib.awtextra.AbsoluteConstraints(365, 130, -1, -1));

        jComboBox1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox1.setForeground(new java.awt.Color(67, 67, 67));
        bid_content.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 380, 220, 40));

        getContentPane().add(bid_content, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 0, 550, 530));

        claim_content.setBackground(new java.awt.Color(67, 67, 67));
        claim_content.setPreferredSize(new java.awt.Dimension(550, 530));
        claim_content.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        claim_title.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        claim_title.setForeground(new java.awt.Color(255, 255, 255));
        claim_title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        claim_title.setText("Claim");
        claim_title.setToolTipText("");
        claim_title.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        claim_content.add(claim_title, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, 70, 40));

        auction_to_bid_label1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        auction_to_bid_label1.setForeground(new java.awt.Color(255, 255, 255));
        auction_to_bid_label1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        auction_to_bid_label1.setText("Auction to claim");
        claim_content.add(auction_to_bid_label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 380, 150, 40));

        auction_to_bid_icon1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        auction_to_bid_icon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_Auction_20px.png"))); // NOI18N
        claim_content.add(auction_to_bid_icon1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 380, 40, 40));

        bid_button1.setBackground(new java.awt.Color(255, 255, 255));
        bid_button1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        bid_button1.setForeground(new java.awt.Color(67, 67, 67));
        bid_button1.setText("Claim");
        bid_button1.setBorderPainted(false);
        bid_button1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bid_button1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bid_button1MouseClicked(evt);
            }
        });
        bid_button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bid_button1ActionPerformed(evt);
            }
        });
        claim_content.add(bid_button1, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 450, 120, -1));

        update_auction_list_button1.setBackground(new java.awt.Color(255, 255, 255));
        update_auction_list_button1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        update_auction_list_button1.setForeground(new java.awt.Color(67, 67, 67));
        update_auction_list_button1.setText("Update list");
        update_auction_list_button1.setBorderPainted(false);
        update_auction_list_button1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        update_auction_list_button1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                update_auction_list_button1MouseClicked(evt);
            }
        });
        update_auction_list_button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                update_auction_list_button1ActionPerformed(evt);
            }
        });
        claim_content.add(update_auction_list_button1, new org.netbeans.lib.awtextra.AbsoluteConstraints(365, 130, -1, -1));

        jComboBox2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox2.setForeground(new java.awt.Color(67, 67, 67));
        claim_content.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 380, 220, 40));

        getContentPane().add(claim_content, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 0, 550, 530));

        all_auction_content.setBackground(new java.awt.Color(67, 67, 67));
        all_auction_content.setPreferredSize(new java.awt.Dimension(550, 530));
        all_auction_content.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        update_allauctions_button.setBackground(new java.awt.Color(255, 255, 255));
        update_allauctions_button.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        update_allauctions_button.setForeground(new java.awt.Color(67, 67, 67));
        update_allauctions_button.setText("Update");
        update_allauctions_button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        update_allauctions_button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                update_allauctions_buttonMouseClicked(evt);
            }
        });
        all_auction_content.add(update_allauctions_button, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 490, -1, 30));

        all_auctions_title.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        all_auctions_title.setForeground(new java.awt.Color(255, 255, 255));
        all_auctions_title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        all_auctions_title.setText("All Auctions");
        all_auctions_title.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        all_auction_content.add(all_auctions_title, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, 150, 40));

        mine_button.setBackground(new java.awt.Color(255, 255, 255));
        mine_button.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        mine_button.setForeground(new java.awt.Color(67, 67, 67));
        mine_button.setText("Mine");
        mine_button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        mine_button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mine_buttonMouseClicked(evt);
            }
        });
        mine_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mine_buttonActionPerformed(evt);
            }
        });
        all_auction_content.add(mine_button, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 130, 80, -1));

        active_button.setBackground(new java.awt.Color(255, 255, 255));
        active_button.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        active_button.setForeground(new java.awt.Color(67, 67, 67));
        active_button.setText("Active");
        active_button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        active_button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                active_buttonMouseClicked(evt);
            }
        });
        all_auction_content.add(active_button, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 130, 80, -1));

        won_active.setBackground(new java.awt.Color(255, 255, 255));
        won_active.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        won_active.setForeground(new java.awt.Color(67, 67, 67));
        won_active.setText("Won");
        won_active.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        won_active.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                won_activeMouseClicked(evt);
            }
        });
        won_active.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                won_activeActionPerformed(evt);
            }
        });
        all_auction_content.add(won_active, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 130, 80, -1));

        getContentPane().add(all_auction_content, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 0, 550, 530));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void createMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_createMouseClicked
        create_content.setVisible(true);
        bid_content.setVisible(false);
        claim_content.setVisible(false);
        home_content.setVisible(false);
        all_auction_content.setVisible(false);
    }//GEN-LAST:event_createMouseClicked

    private void bidMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bidMouseClicked
        create_content.setVisible(false);
        bid_content.setVisible(true);
        claim_content.setVisible(false);
        home_content.setVisible(false);
        all_auction_content.setVisible(false);

        comboBoxTransformBid();
    }//GEN-LAST:event_bidMouseClicked

    private void claimMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_claimMouseClicked
        create_content.setVisible(false);
        bid_content.setVisible(false);
        claim_content.setVisible(true);
        home_content.setVisible(false);
        all_auction_content.setVisible(false);
        
        comboBoxTransformClaim();
        
    }//GEN-LAST:event_claimMouseClicked

    private void homeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homeMouseClicked
        create_content.setVisible(false);
        bid_content.setVisible(false);
        claim_content.setVisible(false);
        home_content.setVisible(true);
        all_auction_content.setVisible(false);
    }//GEN-LAST:event_homeMouseClicked

    private void update_auction_list_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_update_auction_list_buttonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_update_auction_list_buttonActionPerformed

    private void update_auction_list_buttonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_update_auction_list_buttonMouseClicked
        comboBoxTransformBid();
    }//GEN-LAST:event_update_auction_list_buttonMouseClicked

    private void jTextFieldPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldPriceActionPerformed
    private void showAllAuctions(String s){
        
        JTextArea bid_auctions = new JTextArea(s);
        bid_auctions.setEditable(false);
        bid_auctions.setColumns(20);
        bid_auctions.setRows(5);
        allauct.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        if(all_flag == 0){
            allauct.setViewportView(bid_auctions);
            all_auction_content.add(allauct,new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 170, 430, 310));
            all_flag++;
        }
        else allauct.setViewportView(bid_auctions);
        
    }
    private void showClaimAuctions(String s){
        
        JTextArea bid_auctions = new JTextArea(s);
        bid_auctions.setEditable(false);
        bid_auctions.setColumns(20);
        bid_auctions.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        bid_auctions.setForeground(new java.awt.Color(67, 67, 67));
        bid_auctions.setRows(5);
        claimm.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        if(claim_flag == 0){
            claimm.setViewportView(bid_auctions);
            claim_content.add(claimm,new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 170, 420, 190));
            claim_flag++;
        }
        else claimm.setViewportView(bid_auctions);
        
    }
    private void showAuctions(String s){
        
        JTextArea bid_auctions = new JTextArea(s);
        bid_auctions.setEditable(false);
        bid_auctions.setColumns(20);
        bid_auctions.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        bid_auctions.setForeground(new java.awt.Color(67, 67, 67));
        bid_auctions.setRows(5);
        auct.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        if(auction_flag == 0){
            auct.setViewportView(bid_auctions);
            bid_content.add(auct,new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 170, 420, 190));
            auction_flag++;
        }
        else auct.setViewportView(bid_auctions);
        
    }
    private void comboBoxTransformBid() {

        int result = cli.getAuctionList(pub, pk, serverPub);
        System.out.println("Número de leilões: " + result);
        updateflag = 1;
        Map<String, AuctionClient> auctions = cli.getAuctions();
        auctionKeys = new String[auctions.size()];

        jComboBox1.removeAllItems();
        //if (n==3) jComboBox3.removeAllItems();

        StringBuilder sb = new StringBuilder();

        int i = 1;

        for (Map.Entry<String, AuctionClient> entry : auctions.entrySet()) {

            jComboBox1.addItem(Integer.toString(i));
            auctionKeys[i - 1] = entry.getKey();
            sb.append(String.format("Auction %s\n%s", i, entry.getValue().toString()));
            i++;
        }
        showAuctions(sb.toString());

    }

    
    private void comboBoxTransformClaim() {

        int result = cli.getAuctionList(pub, pk, serverPub);
        System.out.println("Número de leilões: " + result);
        updateflag = 1;
        Map<String, AuctionClient> auctions = cli.getAuctions();
        auctionKeys = new String[auctions.size()];

        jComboBox2.removeAllItems();
        //if (n==3) jComboBox3.removeAllItems();

        StringBuilder sb = new StringBuilder();

        int i = 1;

        for (Map.Entry<String, AuctionClient> entry : auctions.entrySet()) {
            if (!entry.getValue().isActive() && pubStringHash.equals(entry.getValue().getWinner())) {
                jComboBox2.addItem(Integer.toString(i));
                auctionKeys[i - 1] = entry.getKey();
                sb.append(String.format("Auction %s\n%s", i, entry.getValue().toString()));
                i++;
            }
            
        }
        showClaimAuctions(sb.toString());

    }


    private void bid_buttonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bid_buttonMouseClicked

        if (updateflag == 0) {
            int result = cli.getAuctionList(pub, pk, serverPub);
            System.out.println("Numero de leilões: " + result);
        }
        
        if (auctionKeys == null) JOptionPane.showMessageDialog(null, "There are no auctions.");

        else {
            String auctionToBid = auctionKeys[jComboBox1.getSelectedIndex()];
            String price = jTextFieldPrice.getText();

            JOptionPane.showMessageDialog(null, cli.getAuctions().get(auctionToBid).toString());

            if (price.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please insert a price before bidding!");
            } else if (!cli.tryParse(price, "DOUBLE")) {
                JOptionPane.showMessageDialog(null, "The price must be a number!");
            } else {

                String result = cli.bid(pub, pk, serverPub, auctionToBid, Double.parseDouble(price));

                if (result == null) {
                    JOptionPane.showMessageDialog(null, "Bid completed!");
                    jTextFieldPrice.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, result);
                }
            }
        }
        
    }//GEN-LAST:event_bid_buttonMouseClicked

    private void homeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homeMouseEntered
        home.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
    }//GEN-LAST:event_homeMouseEntered

    private void createMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_createMouseEntered
        create.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
    }//GEN-LAST:event_createMouseEntered

    private void bidMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bidMouseEntered
        bid.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
    }//GEN-LAST:event_bidMouseEntered

    private void claimMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_claimMouseEntered
        claim.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
    }//GEN-LAST:event_claimMouseEntered

    private void homeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homeMouseExited
        home.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 0));
    }//GEN-LAST:event_homeMouseExited

    private void createMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_createMouseExited
        create.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 0));
    }//GEN-LAST:event_createMouseExited

    private void bidMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bidMouseExited
        bid.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 0));
    }//GEN-LAST:event_bidMouseExited

    private void claimMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_claimMouseExited
        claim.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 0));
    }//GEN-LAST:event_claimMouseExited

    private void update_allauctions_buttonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_update_allauctions_buttonMouseClicked
        cli.getAuctionList(pub, pk, serverPub);
        JOptionPane.showMessageDialog(null, "The auction list has been updated!");

//        if (all_auctions_textarea.getText().isEmpty()) {
//            JOptionPane.showMessageDialog(null, "You dont have any auctions!");
//        } else {
//            JOptionPane.showMessageDialog(null, "Your auction list has been updated!");
//        }
    }//GEN-LAST:event_update_allauctions_buttonMouseClicked

    private void create_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_create_buttonActionPerformed

    }//GEN-LAST:event_create_buttonActionPerformed

    private void create_buttonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_create_buttonMouseClicked

        String desc = jTextFieldDesc.getText();
        String basePrice = jTextFieldBaseprice.getText();
        String time = jTextFieldTime.getText();

        if (basePrice.isEmpty() || desc.isEmpty() || time.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Fill all the fields before creating!");
        } else if (!(cli.tryParse(basePrice, "DOUBLE") && cli.tryParse(time, "LONG"))) {
            JOptionPane.showMessageDialog(null, "The price and duration must be numbers!");
        } else {
            cli.setDescription(desc);
            cli.setTime(time);
            cli.setBasePrice(basePrice);

            String result = cli.createAuction(pub, pk, serverPub);

            if (result == null) {
                JOptionPane.showMessageDialog(null, "Auction creation completed!");
                jTextFieldDesc.setText("");
                jTextFieldBaseprice.setText("");
                jTextFieldTime.setText("");
            } else {
                JOptionPane.showMessageDialog(null, result);
            }
        }
    }//GEN-LAST:event_create_buttonMouseClicked

    private void jTextFieldTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldTimeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldTimeActionPerformed

    private void bid_button1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bid_button1MouseClicked
        if (auctionKeys == null) JOptionPane.showMessageDialog(null, "There are no auctions.");

        else {
            String auctionToBid = auctionKeys[jComboBox2.getSelectedIndex()];

            JOptionPane.showMessageDialog(null, cli.getAuctions().get(auctionToBid).toString());

            String result = cli.claimPrize(pub, pk, serverPub, auctionToBid);

            if (result == null) {
                JOptionPane.showMessageDialog(null, "Verified.");
            } else {
                JOptionPane.showMessageDialog(null, result);
            }
        }
        
    }//GEN-LAST:event_bid_button1MouseClicked

    private void update_auction_list_button1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_update_auction_list_button1MouseClicked
        comboBoxTransformClaim();
    
    }//GEN-LAST:event_update_auction_list_button1MouseClicked

    private void update_auction_list_button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_update_auction_list_button1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_update_auction_list_button1ActionPerformed

    private void all_auctionsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_all_auctionsMouseClicked
        create_content.setVisible(false);
        bid_content.setVisible(false);
        claim_content.setVisible(false);
        home_content.setVisible(false);
        all_auction_content.setVisible(true);
        
        cli.getAuctionList(pub, pk, serverPub);
        showAllAuctions(cli.printMyAuctions());
    }//GEN-LAST:event_all_auctionsMouseClicked

    private void all_auctionsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_all_auctionsMouseEntered
        all_auctions.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
    }//GEN-LAST:event_all_auctionsMouseEntered

    private void all_auctionsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_all_auctionsMouseExited
        all_auctions.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 0));
    }//GEN-LAST:event_all_auctionsMouseExited

    private void mine_buttonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mine_buttonMouseClicked
        JTextArea a =  (JTextArea) allauct.getViewport().getView();
        a.removeAll();
        showAllAuctions(cli.printMyAuctions());
    }//GEN-LAST:event_mine_buttonMouseClicked

    private void active_buttonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_active_buttonMouseClicked

        updateflag = 1;
        Map<String, AuctionClient> auctions = cli.getAuctions();

        StringBuilder sb = new StringBuilder();

        int i = 1;
        for (Map.Entry<String, AuctionClient> entry : auctions.entrySet()) {
            if (entry.getValue().isActive()) sb.append(String.format("Auction %s\n%s", i, entry.getValue().toString()));
            i++;
        }
        showAllAuctions(sb.toString());
    }//GEN-LAST:event_active_buttonMouseClicked

    private void won_activeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_won_activeMouseClicked
        updateflag = 1;
        Map<String, AuctionClient> auctions = cli.getAuctions();

        StringBuilder sb = new StringBuilder();

        int i = 1;
        for (Map.Entry<String, AuctionClient> entry : auctions.entrySet()) {
            if (pubStringHash.equals(entry.getValue().getWinner())) sb.append(String.format("Auction %s\n%s", i, entry.getValue().toString()));
            i++;
        }
        showAllAuctions(sb.toString());
    }//GEN-LAST:event_won_activeMouseClicked

    private void mine_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mine_buttonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mine_buttonActionPerformed

    private void bid_button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bid_button1ActionPerformed
        
    }//GEN-LAST:event_bid_button1ActionPerformed

    private void won_activeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_won_activeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_won_activeActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Index.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Index.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Index.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Index.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Index().setVisible(true);

                } catch (KeyStoreException ex) {
                    Logger.getLogger(Index.class
                            .getName()).log(Level.SEVERE, null, ex);

                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(Index.class
                            .getName()).log(Level.SEVERE, null, ex);

                } catch (CertificateException ex) {
                    Logger.getLogger(Index.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton active_button;
    private javax.swing.JPanel all_auction_content;
    private javax.swing.JPanel all_auctions;
    private javax.swing.JLabel all_auctions_icon;
    private javax.swing.JLabel all_auctions_title;
    private javax.swing.JLabel auction_to_bid_icon;
    private javax.swing.JLabel auction_to_bid_icon1;
    private javax.swing.JLabel auction_to_bid_label;
    private javax.swing.JLabel auction_to_bid_label1;
    private javax.swing.JLabel base_price_icon;
    private javax.swing.JLabel base_price_label;
    private javax.swing.JPanel bid;
    private javax.swing.JButton bid_button;
    private javax.swing.JButton bid_button1;
    private javax.swing.JPanel bid_content;
    private javax.swing.JLabel bid_icon;
    private javax.swing.JLabel bid_label;
    private javax.swing.JLabel bid_title;
    private javax.swing.JPanel claim;
    private javax.swing.JPanel claim_content;
    private javax.swing.JLabel claim_icon;
    private javax.swing.JLabel claim_label;
    private javax.swing.JLabel claim_title;
    private javax.swing.JPanel create;
    private javax.swing.JButton create_button;
    private javax.swing.JPanel create_content;
    private javax.swing.JLabel create_icon;
    private javax.swing.JLabel create_label;
    private javax.swing.JLabel create_title;
    private javax.swing.JLabel desc_icon;
    private javax.swing.JLabel desc_label;
    private javax.swing.JLabel duration_icon;
    private javax.swing.JLabel duration_label;
    private javax.swing.JPanel home;
    private javax.swing.JPanel home_content;
    private javax.swing.JLabel home_icon;
    private javax.swing.JLabel home_label;
    private javax.swing.JLabel home_title;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTextField jTextFieldBaseprice;
    private javax.swing.JTextField jTextFieldDesc;
    private javax.swing.JTextField jTextFieldPrice;
    private javax.swing.JTextField jTextFieldTime;
    private javax.swing.JLabel logo;
    private javax.swing.JButton mine_button;
    private javax.swing.JLabel myauctions_label1;
    private javax.swing.JLabel price_icon;
    private javax.swing.JLabel price_label;
    private javax.swing.JPanel sidepane;
    private javax.swing.JButton update_allauctions_button;
    private javax.swing.JButton update_auction_list_button;
    private javax.swing.JButton update_auction_list_button1;
    private javax.swing.JLabel user_pub_key_label;
    private javax.swing.JButton won_active;
    // End of variables declaration//GEN-END:variables
}
