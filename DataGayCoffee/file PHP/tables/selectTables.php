<?php
require "../dbConnect.php";

try {
    $shop_id = $_POST['shop_id'];

    $conn = new PDO("mysql:host=$host;dbname=$dbname", $username, $password);
    $stmt = $conn->prepare('SELECT 
	t.shop_id,
    t.user_id,
    t.id AS table_id,
    t.table_name,
    t.status,
    t.table_price,
    COALESCE(SUM(oi.quantity), 0) AS product_quantity,
    o.id AS order_id
FROM
    tables t
    
LEFT JOIN
    orders o ON t.id = o.table_id
LEFT JOIN 
    (SELECT 
        order_id,
        SUM(quantity) AS quantity
    FROM 
        order_items
    GROUP BY 
        order_id
    ) AS oi ON o.id = oi.order_id
    
WHERE t.shop_id = :shop_id
GROUP BY
    t.id, t.table_name, t.status, o.id;
   

');


    $stmt->bindParam(':shop_id', $shop_id);
    $stmt->execute();
    $mang = array();

    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        $shop_id = $row['shop_id'];
        $user_id = $row['user_id'];
        $table_id = $row['table_id'];
        $table_name = $row['table_name'];
        $status = $row['status'];
        $table_price = $row['table_price'];
        $product_quantity = $row['product_quantity'];
        $order_id = $row['order_id'];

        array_push($mang, new Tables($shop_id, $user_id, $table_id, $table_name, $status, $table_price, $product_quantity, $order_id));
    }

    echo json_encode($mang);
} catch (PDOException $pe) {
    die("Could not connect to the database $dbname :" . $pe->getMessage());
}

class Tables
{
    public $shop_id;
    public $user_id;
    public $table_id;
    public $table_name;
    public $status;
    public $table_price;
    public $product_quantity;
    public $order_id;

    public function __construct($shop_id, $user_id, $table_id, $table_name, $status, $table_price, $product_quantity, $order_id)
    {
        $this->shop_id = $shop_id;
        $this->user_id = $user_id;
        $this->table_id = $table_id;
        $this->table_name = $table_name;
        $this->status = $status;
        $this->table_price = $table_price;
        $this->product_quantity = $product_quantity;
        $this->order_id = $order_id;
    }
}
