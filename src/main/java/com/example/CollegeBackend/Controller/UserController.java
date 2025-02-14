package com.example.CollegeBackend.Controller;

import com.example.CollegeBackend.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api") // Optional base path
public class UserController {

    private static final String URL = "jdbc:mysql://localhost:3306/college-portal";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    @GetMapping("/allusers")  // Route: http://localhost:8080/api/allusers
    public List<User> sayHello() {
        Connection con = connect();
        System.out.println("Connected to database");
//        String sql = "select * from user";
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int roll = rs.getInt("roll");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String  address = rs.getString("address");
                long  primaryContact  = rs.getLong("primary_contact");
                users.add(new User(roll, firstName, lastName, address, primaryContact));
            }
            rs.close();
            ps.close();
            con.close();
            System.out.println(users);
            return users;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return users;
        }
    }
    public static void insertRecord(Connection conn, String firstName,String lastName, String address,long primaryContact) {
        String sql = "INSERT INTO users (first_name,last_name,primary_contact,address) values (?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setLong(3, primaryContact);
            pstmt.setString(4, address);
            pstmt.executeUpdate();
            System.out.println("Record inserted successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    @GetMapping("/user/new")
    public String addUser() {
        try {

            Connection con = connect();
            for (int i = 0; i < 10; i++) {
                insertRecord(con, "Ram", "Gupta", "Ratnanagar", 988888888);
            }
            con.close();
            return "Data added successfully";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Error adding user";
        }
    }

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }
}


