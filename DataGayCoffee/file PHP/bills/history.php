<?php
require "../dbConnect.php";

try {

    $conn = new PDO("mysql:host=$host;dbname=$dbname", $username, $password);
    $stmt = $conn->prepare('SELECT bills.id,bills.dateTime,bills.dateTime_end,bills.code,bills.table_id,bills.user_id,bills.total_price,table_name FROM `tables`,`bills` WHERE bills.table_id = tables.id;');
    $stmt->execute();
    $mang = array();

    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        $id = $row['id'];
        $dateTime = $row['dateTime'];
        $dateTime_end = $row['dateTime_end'];
        $code = $row['code'];
        $table_id = $row['table_id'];
        $user_id = $row['user_id'];
        $total_price = $row['total_price'];
        $table_name = $row['table_name'];
        array_push($mang, new bills($id, $dateTime, $dateTime_end, $code, $table_id, $user_id, $total_price, $table_name));
    }

    echo json_encode($mang);
} catch (PDOException $pe) {
    die("Could not connect to the database $dbname :" . $pe->getMessage());
}

class bills
{
    public $id;
    public $dateTime;
    public $dateTime_end;
    public $code;
    public $table_id;
    public $user_id;
    public $total_price;
    public $table_name;

    public function __construct($id, $dateTime, $dateTime_end, $code, $table_id, $user_id, $total_price, $table_name)
    {
        $this->id = $id;
        $this->dateTime = $dateTime;
        $this->dateTime_end = $dateTime_end;
        $this->code = $code;
        $this->table_id = $table_id;
        $this->user_id = $user_id;
        $this->total_price = $total_price;
        $this->table_name = $table_name;
    }
}
?>