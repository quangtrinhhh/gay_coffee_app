<?php

try {

    require "../dbConnect.php";

    $id = $_POST['id'];
    $table_id = $_POST['table_id'];

    $stmt = $conn->prepare('update ORDERS set TABLE_ID = :table_id where ID = :id');

    $stmt->bindParam(':id', $id);
    $stmt->bindParam(':table_id', $table_id);

    $stmt->execute();

    echo "update data successful";
} catch (PDOException $pe) {
    die("Could not update data to the database $dbname :" . $pe->getMessage());
}
