<?php

try {
    require "../dbConnect.php";

    $dateTime = $_POST['dateTime'];
    $dateTime_end = $_POST['dateTime_end'];
    $code = $_POST['code'];
    $table_id = $_POST['table_id'];
    $user_id = $_POST['user_id'];
    $total_price = $_POST['total_price'];

    $stmt = $conn->prepare('INSERT INTO BILLS (`dateTime`, `dateTime_end`, `code`, `table_id`, `user_id`,`total_price`) VALUES (:dateTime, :dateTime_end, :code, :table_id, :user_id, :total_price)');

    $stmt->bindParam(':dateTime', $dateTime);
    $stmt->bindParam(':dateTime_end', $dateTime_end);
    $stmt->bindParam(':code', $code);
    $stmt->bindParam(':table_id', $table_id);
    $stmt->bindParam(':user_id', $user_id);
    $stmt->bindParam(':total_price', $total_price);

    $stmt->execute();

    // Lấy Order ID vừa được thêm vào
    $billId = $conn->lastInsertId();

    // Trả về Order ID dưới dạng phản hồi JSON
    echo json_encode(array('billId' => $billId));


    echo "insert data successful";
} catch (PDOException $pe) {
    die("Could not insert data to the database $dbname :" . $pe->getMessage());
}
