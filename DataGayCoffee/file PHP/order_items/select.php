<?php
require "../dbConnect.php";

try {

    $conn = new PDO("mysql:host=$host;dbname=$dbname", $username, $password);
    $stmt = $conn->prepare('SELECT * from ORDER_ITEMS');
    $stmt->execute();
    $mang = array();

    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        $id = $row['id'];
        $quantity = $row['quantity'];
        $price = $row['price'];
        $order_id = $row['order_id'];
        $product_id = $row['product_id'];
        array_push($mang, new OrderItems($id, $quantity, $price, $order_id, $product_id));
    }

    echo json_encode($mang);
} catch (PDOException $pe) {
    die("Could not connect to the database $dbname :" . $pe->getMessage());
}

class OrderItems
{
    public $id;
    public $quantity;
    public $price;
    public $order_id;
    public $product_id;

    public function __construct($id, $quantity, $price, $order_id, $product_id)
    {
        $this->id = $id;
        $this->quantity = $quantity;
        $this->price = $price;
        $this->order_id = $order_id;
        $this->product_id = $product_id;
    }
}
