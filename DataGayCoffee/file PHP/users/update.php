<?php

try {

    require "../dbConnect.php";

    $id = $_POST['id'];
    $name = $_POST['name'];
    $password = $_POST['password'];
    $restaurant_name = $_POST['restaurant_name'];
    $role = $_POST['role'];


    $stmt = $conn->prepare('update ORDERS set NAME = :name, PASSWORD = :password, restaurant_name = :restaurant_name, ROLE = :role where ID = :id');

    $stmt->bindParam(':id', $id);
    $stmt->bindParam(':name', $name);
    $stmt->bindParam(':password', $password);
    $stmt->bindParam(':restaurant_name', $restaurant_name);
    $stmt->bindParam(':role', $role);

    $stmt->execute();

    echo "update data successful";
} catch (PDOException $pe) {
    die("Could not update data to the database $dbname :" . $pe->getMessage());
}
