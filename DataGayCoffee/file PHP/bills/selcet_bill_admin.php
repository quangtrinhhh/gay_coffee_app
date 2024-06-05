<?php
require "../dbConnect.php";

class Bill
{
    public $id_bill;
    public $dateTime;
    public $dateTimeEnd;
    public $code;
    public $tableId;
    public $userId;
    public $total_price;
    public $name_user;
    public $id_shop;

    public function __construct($id_bill, $dateTime, $dateTimeEnd, $code, $tableId, $userId, $total_price, $name_user, $id_shop)
    {
        $this->id_bill = $id_bill;
        $this->dateTime = $dateTime;
        $this->dateTimeEnd = $dateTimeEnd;
        $this->code = $code;
        $this->tableId = $tableId;
        $this->userId = $userId;
        $this->total_price = $total_price;
        $this->name_user = $name_user;
        $this->id_shop = $id_shop;
    }
}

try {

    $shop_id = $_POST['shop_id'];

    $conn = new PDO("mysql:host=$host;dbname=$dbname", $username, $password);
    $stmt = $conn->prepare('SELECT bills.id, bills.code, bills.dateTime, bills.dateTime_end,bills.table_id,bills.total_price,users.id as user_id, users.name as name_user ,shops.id as id_shop
    FROM
    bills INNER JOIN users ON
    bills.user_id = users.id INNER JOIN shops ON shops.user_id = users.id
    WHERE shops.id = :shop_id;');


    $stmt->bindParam(':shop_id', $shop_id);
    $stmt->execute();
    $billArray = array();

    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        $id_bill = $row['id'];
        $dateTime = $row['dateTime'];
        $dateTimeEnd = $row['dateTime_end'];
        $code = $row['code'];
        $tableId = $row['table_id'];
        $userId = $row['user_id'];
        $total_price = $row['total_price'];
        $name_user = $row['name_user'];
        $id_shop = $row['id_shop'];

        array_push($billArray, new Bill($id_bill, $dateTime, $dateTimeEnd, $code, $tableId, $userId, $total_price, $name_user, $id_shop));
    }

    echo json_encode($billArray);
} catch (PDOException $pe) {
    die("Could not connect to the database $dbname :" . $pe->getMessage());
}
