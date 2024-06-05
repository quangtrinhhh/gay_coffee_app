<?php

try {
    require "../dbConnect.php";

    // Các biến đăng ký cố định
    $shop_name = $_POST['name_shop'];
    $user_name = $_POST['name_user'];
    $password = $_POST['password'];
    $role = "order";

    // Thêm người dùng mới
    $stmtUser = $conn->prepare("INSERT INTO users (name, password, role) VALUES (:name, :password, :role)");
    $stmtUser->bindParam(':name', $user_name);
    $stmtUser->bindParam(':password', $password);
    $stmtUser->bindParam(':role', $role);
    $stmtUser->execute();

    // Lấy ID của người dùng vừa thêm vào cơ sở dữ liệu
    $user_id = $conn->lastInsertId();

    // Tạo 50 bàn cho người dùng mới
    for ($i = 0; $i <= 50; $i++) {
        if ($i == 0) {
            $table_name = "Mang về ";
        } else {
            $table_name = "BÀN " . $i;
        }
        $stmtTable = $conn->prepare("INSERT INTO tables (table_name, status, user_id, table_price) VALUES (:table_name, 0, :user_id, 0)");
        $stmtTable->bindParam(':table_name', $table_name);
        $stmtTable->bindParam(':user_id', $user_id);
        $stmtTable->execute();
    }

    // Thêm cửa hàng mới với ID của người dùng
    $stmtShop = $conn->prepare("INSERT INTO shops (name, user_id) VALUES (:name, :user_id)");
    $stmtShop->bindParam(':name', $shop_name);
    $stmtShop->bindParam(':user_id', $user_id);
    $stmtShop->execute();

    echo "Insert data successful";
} catch (PDOException $pe) {
    die("Could not insert data to the database: " . $pe->getMessage());
}
?>