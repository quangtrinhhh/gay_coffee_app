<?php

try {
    require "../dbConnect.php";

    $dateTime = $_POST['dateTime'];
    $code = $_POST['code'];
    $table_id = $_POST['table_id'];
    $user_id = $_POST['user_id'];

 $stmt = $conn->prepare('INSERT INTO ORDERS (`dateTime`,code,table_id,user_id) values (:dateTime,:code,:table_id,:user_id)');

    $stmt->bindParam(':dateTime', $dateTime);
    $stmt->bindParam(':code', $code);
    $stmt->bindParam(':table_id', $table_id);
    $stmt->bindParam(':user_id', $user_id);

    $stmt->execute();

  // Lấy Order ID vừa được thêm vào
    $orderId = $conn->lastInsertId();

    // Trả về Order ID dưới dạng phản hồi JSON
    echo json_encode(array('orderId' => $orderId));

    echo "insert data successful";
} catch (PDOException $pe) {
    die("Could not insert data to the database $dbname :" . $pe->getMessage());
}
