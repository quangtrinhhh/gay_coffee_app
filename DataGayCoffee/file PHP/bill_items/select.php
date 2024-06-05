<?php
require "../dbConnect.php";

try {
    $conn = new PDO("mysql:host=$host;dbname=$dbname", $username, $password);
    $stmt = $conn->prepare('SELECT * FROM BILL_ITEMS');
    $stmt->execute();
    $billArray = array();

    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        $id = $row['id'];
        $quantity = $row['quantity'];
        $total_price = $row['total_price'];
        $product_id = $row['product_id'];
        $bill_id = $row['bill_id'];
        array_push($billArray, new Bill_ITEMS($id, $quantity, $total_price, $product_id, $bill_id));
    }

    echo json_encode($billArray);
} catch (PDOException $pe) {
    die("Could not connect to the database $dbname :" . $pe->getMessage());
}

class Bill_ITEMS
{
    public $id;
    public $quantity;
    public $total_price;
    public $product_id;
    public $bill_id;

    public function __construct($id, $quantity, $total_price, $product_id, $bill_id)
    {
        $this->id = $id;
        $this->quantity = $quantity;
        $this->total_price = $total_price;
        $this->product_id = $product_id;
        $this->bill_id = $bill_id;
    }
}
