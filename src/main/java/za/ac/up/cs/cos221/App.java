package za.ac.up.cs.cos221;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import java.util.regex.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class App 
{
    private static String driver = "jdbc:mariadb";
    private static String host = "";
    private static int port = 3306;
    private static String username = "";
    private static String password = "";
    private static String database = "u21555258_sakila";

    public static void main( String[] args ) throws SQLException {

        final String url = new StringBuilder()
                .append(driver).append("://")
                .append(host).append(":").append(port).append("/")
                .append(database)
                .toString();

        try (final Connection connection = DriverManager.getConnection(url, username, password)) {

            try(Statement statement = connection.createStatement()) {

                final JFrame f;
                f = new JFrame();
                //Top pane
                JPanel staff = new JPanel();
                JPanel films = new JPanel();
                JPanel inventory = new JPanel();
                JPanel clients = new JPanel();
                JTabbedPane table = new JTabbedPane();
                table.setBounds(10, 10, 1500, 900);
                table.add("Staff", staff);
                table.add("Films", films);
                table.add("Inventory", inventory);
                table.add("Clients", clients);


                // Staff table
                String staf_column[] = {"First Name", "Last Name", "Address1", "Address2", "District",
                        "City", "Postal", "Phone", "Store", "Active"};

                final JTextField staff_filter = new JTextField(20);
                DefaultTableModel staff_table_model = new DefaultTableModel();
                staff_table_model.setColumnIdentifiers(staf_column);
                JTable staff_box = new JTable();
                staff_box.setModel(staff_table_model);
                final TableRowSorter<TableModel> staff_sort = new TableRowSorter<>(staff_box.getModel());
                staff_box.setRowSorter(staff_sort);

                try (ResultSet staff_return = statement.executeQuery("SELECT first_name, last_name, address, address2, district, city, postal_code, phone, store_id, active\n" +
                        "FROM ((staff \n" +
                        "INNER JOIN address ON staff.address_id = address.address_id)\n" +
                        "INNER JOIN city ON address.city_id = city.city_id);")) {
                    while (staff_return.next()) {
                        staff_table_model.addRow(new Object[]{staff_return.getString("first_name"), staff_return.getString("last_name"),
                                staff_return.getString("address"), staff_return.getString("address2"),
                                staff_return.getString("district"), staff_return.getString("city"),
                                staff_return.getString("postal_code"), staff_return.getString("phone"),
                                staff_return.getString("store_id"), staff_return.getString("active")});
                    }

                }


                //staff.setLayout(new BoxLayout(staff, BoxLayout.PAGE_AXIS));
                staff.setLayout(new BorderLayout());
                JScrollPane staff_scroll = new JScrollPane(staff_box);
                staff_scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                staff_scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                JPanel staff_filter_panel = new JPanel(new BorderLayout());

                JButton staff_button = new JButton("Filter");
                staff_button.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        String text = staff_filter.getText();
                        if (text.length() == 0) {
                            staff_sort.setRowFilter(null);
                        } else {
                            try {
                                staff_sort.setRowFilter(RowFilter.regexFilter(text));
                            } catch (PatternSyntaxException pse) {
                                System.out.println("Bad regex pattern");
                            }
                        }
                    }
                });
                staff_filter_panel.add(staff_button, BorderLayout.EAST);
                staff_filter_panel.add(new JLabel("Filter the output: "), BorderLayout.WEST);
                staff_filter_panel.add(staff_filter, BorderLayout.CENTER);
                staff.add(staff_filter_panel, BorderLayout.NORTH);
                staff.add(staff_scroll, BorderLayout.CENTER);


                //FIlms tab
                try (PreparedStatement film_get =
                             connection.prepareStatement("SELECT film_id, title, release_year, rental_duration, rental_rate, replacement_cost, rating FROM film;")){

                    String film_column[] = {"Film id", "Title", "Release year", "Rental Duration", "Rental Rate",
                            "Replacement Cost", "Rating"};

                    final DefaultTableModel films_table_model = new DefaultTableModel();
                    films_table_model.setColumnIdentifiers(film_column);
                    JTable film_box = new JTable();
                    film_box.setModel(films_table_model);
                    films.setLayout(new BoxLayout(films, BoxLayout.LINE_AXIS));
                    JScrollPane film_scroll = new JScrollPane(film_box);
                    films.add(film_scroll);
                    JButton films_button = new JButton();
                    films_button.setText("Add film");
                    films.add(films_button);

                    film_get.execute();
                    ResultSet film_get_results = film_get.getResultSet();
                    while (film_get_results.next()) {
                        films_table_model.addRow(new Object[]{film_get_results.getString("film_id"), film_get_results.getString("title"),
                                film_get_results.getString("release_year"), film_get_results.getString("rental_duration"),
                                film_get_results.getString("rental_rate"), film_get_results.getString("replacement_cost"),
                                film_get_results.getString("rating")});
                    }


                    final JPanel films_input = new JPanel(new GridLayout(22, 1));
                    final JTextField film_title = new JTextField(30);
                    final JTextField film_desc = new JTextField(30);
                    final JTextField film_relyear = new JTextField(30);
                    final JTextField film_lanid = new JTextField(30);
                    final JTextField film_rentaldur = new JTextField(30);
                    final JTextField film_rentalrate = new JTextField(30);
                    final JTextField film_length = new JTextField(30);
                    final JTextField film_replacecost = new JTextField(30);
                    final JTextField film_rating = new JTextField(30);
                    final JTextField film_feature = new JTextField(30);
                    //final JTextField film_date = new JTextField(30);
                    films_input.add(new JLabel("Title: "));
                    films_input.add(film_title);
                    films_input.add(new JLabel("Description: "));
                    films_input.add(film_desc);
                    films_input.add(new JLabel("Release Year: "));
                    films_input.add(film_relyear);
                    films_input.add(new JLabel("Language id: "));
                    films_input.add(film_lanid);
                    films_input.add(new JLabel("Rental Duration: "));
                    films_input.add(film_rentaldur);
                    films_input.add(new JLabel("Rental Rate: "));
                    films_input.add(film_rentalrate);
                    films_input.add(new JLabel("Length: "));
                    films_input.add(film_length);
                    films_input.add(new JLabel("Replacement Cost: "));
                    films_input.add(film_replacecost);
                    films_input.add(new JLabel("Rating: "));
                    films_input.add(film_rating);
                    films_input.add(new JLabel("Special Features: [Trailers, Commentaries, Deleted Scene, Behind the Scenes]"));
                    films_input.add(film_feature);
                    //films_input.add(new JLabel("Date (yyyy-mm-dd hh:mm:ss) : "));
                    //films_input.add(film_date);

                    films_button.addActionListener(new java.awt.event.ActionListener() {
                        @Override
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            JOptionPane.showMessageDialog(null, films_input);
                            try {
                                Connection connection = DriverManager.getConnection(url, username, password);
                                PreparedStatement film_insert = connection.prepareStatement(
                                "INSERT INTO film (title, description, release_year, language_id, rental_duration, rental_rate, length, replacement_cost, rating, special_features) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
                                PreparedStatement film_get = connection.prepareStatement(
                                        "SELECT film_id, title, release_year, rental_duration, rental_rate, replacement_cost, rating FROM film;");
                                if(film_title.getText() != "" && (film_feature.getText().equals("Trailers") || film_feature.getText().equals("Commentaries") ||
                                        film_feature.getText().equals("Deleted Scene") || film_feature.getText().equals("Behind the Scenes"))) {

                                    film_insert.setString(1, film_title.getText());
                                    film_insert.setString(2, film_desc.getText());
                                    film_insert.setString(3, film_relyear.getText());
                                    film_insert.setString(4, film_lanid.getText());
                                    film_insert.setString(5, film_rentaldur.getText());
                                    film_insert.setString(6, film_rentalrate.getText());
                                    film_insert.setString(7, film_length.getText());
                                    film_insert.setString(8, film_replacecost.getText());
                                    film_insert.setString(9, film_rating.getText());
                                    film_insert.setString(10, film_feature.getText());
                                    //film_insert.setString(11, film_date.getText());
                                    film_insert.execute();
                                    films_table_model.setRowCount(0);
                                    film_get.execute();
                                    ResultSet film_get_results = film_get.getResultSet();
                                    while (film_get_results.next()) {
                                        films_table_model.addRow(new Object[]{film_get_results.getString("film_id"), film_get_results.getString("title"),
                                                film_get_results.getString("release_year"), film_get_results.getString("rental_duration"),
                                                film_get_results.getString("rental_rate"), film_get_results.getString("replacement_cost"),
                                                film_get_results.getString("rating")});
                                    }
                                    JOptionPane.showMessageDialog(null, "Success! ");
                                }
                                else{
                                    JOptionPane.showMessageDialog(null, "Please enter the correct values");
                                }
                            } catch (SQLException e) {
                                JOptionPane.showMessageDialog(null, "Sql exception: " + e);
                                System.out.println("Sql exception: " + e);
                            }

                        }
                    });
                }

                //inventory
                String inven_column[] = {"Store Name", "Genre Name", "Number of Movies"};
                String inv_count = "SELECT \n" +
                        "        CONCAT(c.city, _utf8mb4 ',', cy.country) AS store,\n" +
                        "        cat.name AS genre,\n" +
                        "        COUNT(cat.category_id) AS number_count\n" +
                        "        \n" +
                        "    FROM\n" +
                        "        ((((((category `cat`\n" +
                        "        INNER JOIN film_category `fc` ON (cat.category_id = fc.category_id))\n" +
                        "        INNER JOIN inventory `i` ON (fc.film_id = i.film_id))\n" +
                        "        INNER JOIN store `s` ON (i.store_id = s.store_id))\n" +
                        "        INNER JOIN address `a` ON (s.address_id = a.address_id))\n" +
                        "        INNER JOIN city `c` ON (a.city_id = c.city_id))\n" +
                        "        INNER JOIN country `cy` ON (c.country_id = cy.country_id))\n" +
                        "\tGROUP BY cat.name, s.store_id\n" +
                        "\tORDER BY s.store_id, cat.name;";

                final DefaultTableModel inven_table_model = new DefaultTableModel();
                inven_table_model.setColumnIdentifiers(inven_column);
                JTable inven_box = new JTable();
                inven_box.setModel(inven_table_model);
                inventory.setLayout(new BoxLayout(inventory, BoxLayout.LINE_AXIS));
                JScrollPane inven_scroll =new JScrollPane(inven_box);
                inventory.add(inven_scroll);

                try(ResultSet inv_return = statement.executeQuery(inv_count)){
                    while (inv_return.next()) {
                        inven_table_model.addRow(new Object[]{inv_return.getString("store"), inv_return.getString("genre"),
                                inv_return.getString("number_count")});
                    }
                }

                //Clients


                JPanel clients_options = new JPanel();
                clients.setLayout(new BorderLayout());
                clients_options.setLayout(new GridLayout(4, 1));

                JButton client_create = new JButton("Create new client");
                JButton client_update = new JButton("Update client");
                JButton client_delete = new JButton("Delete client");
                JButton client_list = new JButton("List all clients");
                clients_options.add(client_create);
                clients_options.add(client_update);
                clients_options.add(client_delete);
                clients_options.add(client_list);

                clients.add(clients_options);

                //Create client popup message
                final JPanel create_client = new JPanel(new GridLayout(24, 1));
                final JTextField client_storeid = new JTextField(30);
                final JTextField client_firstname = new JTextField(30);
                final JTextField client_lastname = new JTextField(30);
                final JTextField client_email = new JTextField(30);
                final JTextField client_address1 = new JTextField(30);
                final JTextField client_address2 = new JTextField(30);
                final JTextField client_district = new JTextField(30);
                final JTextField client_city = new JTextField(30);
                final JTextField client_postal = new JTextField(30);
                final JTextField client_phone = new JTextField(30);
                final JTextField client_active = new JTextField(30);
                final JTextField client_date = new JTextField(30);
                create_client.add(new Label("Store id: "));
                create_client.add(client_storeid);
                create_client.add(new Label("First Name: "));
                create_client.add(client_firstname);
                create_client.add(new Label("Last Name: "));
                create_client.add(client_lastname);
                create_client.add(new Label("Email: "));
                create_client.add(client_email);
                create_client.add(new Label("Address1: "));
                create_client.add(client_address1);
                create_client.add(new Label("Address2: "));
                create_client.add(client_address2);
                create_client.add(new Label("District"));
                create_client.add(client_district);
                create_client.add(new Label("City id"));
                create_client.add(client_city);
                create_client.add(new Label("Postal Code"));
                create_client.add(client_postal);
                create_client.add(new Label("Phone number"));
                create_client.add(client_phone);
                create_client.add(new Label("Active status (1/0)"));
                create_client.add(client_active);
                //create_client.add(new Label("Current Date (yyyy-mm-dd hh:mm:ss)"));
                //create_client.add(client_date);



                client_create.addActionListener(new java.awt.event.ActionListener(){
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt){
                        JOptionPane.showMessageDialog(null, create_client);
                        try {
                            Connection connection = DriverManager.getConnection(url, username, password);
                            PreparedStatement client_address_stat = connection.prepareStatement(
                                    "INSERT INTO address (address, address2, district, city_id, postal_code, phone) VALUES (?, ?, ?, ?, ?, ?);");
                            PreparedStatement client_address_get = connection.prepareStatement(
                                    "SELECT address_id FROM address WHERE address = ? AND city_id = ?;");
                            PreparedStatement client_create_stat = connection.prepareStatement(
                                    "INSERT INTO customer (store_id, first_name, last_name, email, address_id, active) VALUES (?, ?, ?, ?, ?, ?);");
                            if(client_storeid.getText() != "" && client_firstname.getText() != "" && client_lastname.getText()  != "" &&
                                    client_email.getText() != "" && client_address1.getText() != "" && client_district.getText() != "" &&
                                    client_city.getText() != "" && client_active.getText() != "" && client_date.getText() != "") {

                                client_address_stat.setString(1, client_address1.getText());
                                client_address_stat.setString(2, client_address2.getText());
                                client_address_stat.setString(3, client_district.getText());
                                client_address_stat.setString(4, client_city.getText());
                                client_address_stat.setString(5, client_postal.getText());
                                client_address_stat.setString(6, client_phone.getText());
                                //client_address_stat.setString(7, client_date.getText());
                                client_address_stat.execute();


                                client_address_get.setString(1, client_address1.getText());
                                client_address_get.setString(2, client_city.getText());
                                client_address_get.execute();
                                ResultSet client_address_id_results = client_address_get.getResultSet();
                                client_address_id_results.next();
                                String client_address_id = client_address_id_results.getString("address_id");

                                client_create_stat.setString(1, client_storeid.getText());
                                client_create_stat.setString(2, client_firstname.getText());
                                client_create_stat.setString(3, client_lastname.getText());
                                client_create_stat.setString(4, client_email.getText());
                                client_create_stat.setString(5, client_address_id);
                                client_create_stat.setString(6, client_active.getText());
                                //client_create_stat.setString(7, client_date.getText());
                                //client_create_stat.setString(8, client_date.getText());
                                client_create_stat.execute();
                                JOptionPane.showMessageDialog(null, "Success! ");
                            }
                            else{
                                JOptionPane.showMessageDialog(null, "Please enter the correct values");
                            }
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, "Sql exception: " + e);
                            System.out.println("Sql exception: " + e);
                        }
                    }
                });

                //Client Update popup
                final JPanel update_client = new JPanel(new GridLayout(8, 1));
                final JTextField update_client_email = new JTextField(30);
                final JTextField update_attribute = new JTextField(30);
                final JTextField update_value = new JTextField(30);
                final JTextField update_date = new JTextField(30);
                update_client.add(new Label("Email of client"));
                update_client.add(update_client_email);
                update_client.add(new Label("What to update: "));
                update_client.add(update_attribute);
                update_client.add(new Label("New Value: "));
                update_client.add(update_value);
                //update_client.add(new Label("Current date: (yyyy-mm-dd hh:mm:ss)"));
                //update_client.add(update_date);


                client_update.addActionListener(new java.awt.event.ActionListener(){
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt){
                        JOptionPane.showMessageDialog(null, update_client);
                        try {
                            Connection connection = DriverManager.getConnection(url, username, password);
                            PreparedStatement client_get_id = connection.prepareStatement(
                                    "SELECT customer_id FROM customer WHERE email = ?;");

                            // Check what to update
                            Boolean is_address_update = false;
                            Boolean is_client_update = false;
                            String[] update_address_values = {"address", "address2", "district", "city_id", "postal_code", "phone"};
                            String[] update_client_values = {"store_id", "first_name", "last_name", "email", "active"};
                            for (String str: update_address_values) {
                                if(str.equals(update_attribute.getText())){
                                   is_address_update = true;
                                }
                            }
                            for(String str: update_client_values){
                                if(str.equals(update_attribute.getText())){
                                    is_client_update = true;
                                }
                            }

                            //Update what is selected

                            if(is_address_update && update_client_email.getText() != "" && update_value.getText() != "" && update_date.getText() != "") {

                                PreparedStatement client_update_address = connection.prepareStatement(
                                        "UPDATE (customer INNER JOIN address ON customer.address_id = address.address_id) SET " + update_attribute.getText() + " = ? WHERE customer.customer_id = ?;");

                                client_get_id.setString(1, update_client_email.getText());
                                client_get_id.execute();
                                ResultSet client_id_result = client_get_id.getResultSet();
                                client_id_result.next();
                                String client_update_id = client_id_result.getString("customer_id");
                                System.out.println(client_update_id);
                                //client_update_address.setString(1, update_attribute.getText());
                                client_update_address.setString(1, update_value.getText());
                                //client_update_address.setString(2, update_date.getText());
                                //client_update_address.setString(3, update_date.getText());
                                client_update_address.setString(2, client_update_id);
                                client_update_address.execute();

                                JOptionPane.showMessageDialog(null, "Success! Updated: " + update_attribute.getText());
                            }
                            else if(is_client_update && update_client_email.getText() != "" && update_value.getText() != "" && update_date.getText() != ""){

                                PreparedStatement client_update_client = connection.prepareStatement(
                                        "UPDATE customer SET " + update_attribute.getText() + " = ? WHERE customer_id = ?;");
                                client_get_id.setString(1, update_client_email.getText());
                                client_get_id.execute();
                                ResultSet client_id_result = client_get_id.getResultSet();
                                client_id_result.next();
                                String client_update_id = client_id_result.getString("customer_id");

                                //client_update_client.setString(1, update_attribute.getText());
                                client_update_client.setString(1, update_value.getText());
                                //client_update_client.setString(2, update_date.getText());
                                client_update_client.setString(2, client_update_id);
                                client_update_client.execute();

                                JOptionPane.showMessageDialog(null, "Success! Updated: " + update_attribute.getText());

                            }
                            else{
                                JOptionPane.showMessageDialog(null, "Please enter the correct attribute to update: [address, address2, district, city_id, postal_code, phone, store_id, first_name, last_name, email, active]");
                            }
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, "Please make sure that email exists");
                            System.out.println("Sql exception: " + e);
                        }
                    }
                });

               //Client Delete

                final JPanel delete_client = new JPanel(new GridLayout(6, 1));
                final JTextField delete_name = new JTextField(30);
                final JTextField delete_surname = new JTextField(30);
                final JTextField delete_email = new JTextField(30);
                delete_client.add(new JLabel("Name of user to delete: "));
                delete_client.add(delete_name);
                delete_client.add(new JLabel("Surname of user to delete: "));
                delete_client.add(delete_surname);
                delete_client.add(new JLabel("Email of user to delete: "));
                delete_client.add(delete_email);


                client_delete.addActionListener(new java.awt.event.ActionListener(){
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt){
                        JOptionPane.showMessageDialog(null, delete_client);
                        try {
                            Connection connection = DriverManager.getConnection(url, username, password);
                            PreparedStatement client_delete_stat = connection.prepareStatement(
                                    "DELETE FROM customer WHERE first_name = ? AND last_name = ? AND email = ?;");
                            if(delete_name.getText() != "" && delete_surname.getText() != "" && delete_email.getText() != ""){
                                client_delete_stat.setString(1, delete_name.getText());
                                client_delete_stat.setString(2, delete_surname.getText());
                                client_delete_stat.setString(3, delete_email.getText());
                                client_delete_stat.execute();

                                JOptionPane.showMessageDialog(null, "Deleted user!");
                            }
                            else{
                                JOptionPane.showMessageDialog(null, "Please provide name, surname and email");
                            }
                        }
                        catch (SQLException e){
                            JOptionPane.showMessageDialog(null, "User not found");
                        }
                    }
                });

                //List all users

                String[] users_column = {"id", "Store id", "First Name", "Last Name", "Email", "Address id",
                                         "Active", "Create Date", "Last Update Date"};
                final JPanel clients_all = new JPanel(new BorderLayout());
                final DefaultTableModel clients_all_model = new DefaultTableModel();
                clients_all_model.setColumnIdentifiers(users_column);
                JTable clients_all_table = new JTable();
                clients_all_table.setModel(clients_all_model);
                JScrollPane clients_all_scroll =new JScrollPane(clients_all_table);
                clients_all_scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                clients_all_scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                clients_all_scroll.setPreferredSize(new Dimension(1400, 850));
                clients_all.add(clients_all_scroll);



                client_list.addActionListener(new java.awt.event.ActionListener(){
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt){
                        try{

                            Connection connection = DriverManager.getConnection(url, username, password);
                            PreparedStatement list_all_clients = connection.prepareStatement(
                                    "SELECT * FROM customer");
                            list_all_clients.execute();
                            ResultSet all_clients = list_all_clients.getResultSet();
                            clients_all_model.setRowCount(0);
                            while (all_clients.next()) {
                                clients_all_model.addRow(new Object[]{all_clients.getString("customer_id"), all_clients.getString("store_id"),
                                        all_clients.getString("first_name"), all_clients.getString("last_name"),
                                        all_clients.getString("email"), all_clients.getString("address_id"),
                                        all_clients.getString("active"), all_clients.getString("create_date"),
                                        all_clients.getString("last_update")});
                            }
                            JOptionPane.showMessageDialog(null, clients_all);
                        }
                        catch (SQLException e){
                            JOptionPane.showMessageDialog(null, e);
                        }
                    }
                });

                f.add(table);
                f.setSize(1600,1050);
                f.setLayout(null);
                f.setVisible(true);
            }
        }
    }
}
