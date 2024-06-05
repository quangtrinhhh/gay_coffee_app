<?php
require "../dbConnect.php";

try {
    $user_id_account = $_POST['user_id'];
    $table_name_account = $_POST['table_name'];

    $conn = new PDO("mysql:host=$host;dbname=$dbname", $username, $password);
    $stmt = $conn->prepare('SELECT * FROM `tables` WHERE  `user_id` = :user_id_account AND `table_name` != :table_name_account');

    $stmt->bindParam(':user_id_account', $user_id_account);
    $stmt->bindParam(':table_name_account', $table_name_account);

    $stmt->execute();
    $mang = array();

    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        $id = $row['id'];
        $table_name = $row['table_name'];
        $status = $row['status'];
        $user_id = $row['user_id'];
        $table_price = $row['table_price'];
        array_push($mang, new Tables($id, $table_name, $status, $user_id, $table_price));
    }

    echo json_encode($mang);
} catch (PDOException $pe) {
    die("Could not connect to the database $dbname :" . $pe->getMessage());
}

class Tables
{
    public $id;
    public $table_name;
    public $status;
    public $user_id;
    public $table_price;

    public function __construct($id, $table_name, $status, $user_id, $table_price)
    {
        $this->id = $id;
        $this->table_name = $table_name;
        $this->status = $status;
        $this->user_id = $user_id;
        $this->table_price = $table_price;
    }
}
