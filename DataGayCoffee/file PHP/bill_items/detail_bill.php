<?php
require "../dbConnect.php";

class Bill
{
    public $bill_item_id;
    public $quantity;
    public $total_price;
    public $product_id;
    public $bill_id;
    public $product_name;

    public function __construct($bill_item_id, $quantity, $total_price, $product_id, $bill_id, $product_name)
    {
        $this->bill_item_id = $bill_item_id;
        $this->quantity = $quantity;
        $this->total_price = $total_price;
        $this->product_id = $product_id;
        $this->bill_id = $bill_id;
        $this->product_name = $product_name;
    }
}

try {
    $conn = new PDO("mysql:host=$host;dbname=$dbname", $username, $password);
    $stmt = $conn->prepare('SELECT bill_items.id AS bill_item_id, bill_items.quantity, bill_items.total_price, bill_items.product_id, bill_items.bill_id, products.name AS product_name
    FROM bill_items 
    INNER JOIN products ON bill_items.product_id = products.id 
    INNER JOIN bills ON bill_items.bill_id = bills.id');
    $stmt->execute();
    $bills = array();

    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        $bill_item_id = $row['bill_item_id'];
        $quantity = $row['quantity'];
        $total_price = $row['total_price'];
        $product_id = $row['product_id'];
        $bill_id = $row['bill_id'];
        $product_name = $row['product_name'];
        array_push($bills, new Bill($bill_item_id, $quantity, $total_price, $product_id, $bill_id, $product_name));
    }

    echo json_encode($bills);
} catch (PDOException $pe) {
    die("Could not connect to the database $dbname: " . $pe->getMessage());
}
?>