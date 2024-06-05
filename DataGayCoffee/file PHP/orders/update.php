<?php

try {

    require "../dbConnect.php";

    $id = $_POST['id'];
    $dateTime = $_POST['dateTime'];
    $code = $_POST['code'];
    $table_id = $_POST['table_id'];
    $user_id = $_POST['user_id'];


    $stmt = $conn->prepare('update ORDERS set DATETIME = :dateTime, CODE = :code, TABLE_ID = :table_id, USER_ID = :user_id where ID = :id');

    $stmt->bindParam(':id', $id);
    $stmt->bindParam(':dateTime', $dateTime);
    $stmt->bindParam(':code', $code);
    $stmt->bindParam(':table_id', $table_id);
    $stmt->bindParam(':user_id', $user_id);

    $stmt->execute();

    echo "update data successful";
} catch (PDOException $pe) {
    die("Could not update data to the database $dbname :" . $pe->getMessage());
}
