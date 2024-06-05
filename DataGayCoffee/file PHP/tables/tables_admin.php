<?php
require "../dbConnect.php";

try {
    $shop_id = $_POST['shop_id'];

    $conn = new PDO("mysql:host=$host;dbname=$dbname", $username, $password);
    $stmt = $conn->prepare('
    SELECT
    t.shop_id AS id_shop,
    t.id AS id_table,
    t.table_name AS name_table,
    SUM(oi.quantity * p.price) AS total_table,
    t.status
FROM
    tables t
LEFT JOIN
    orders o ON t.id = o.table_id
LEFT JOIN
    order_items oi ON o.id = oi.order_id
LEFT JOIN
    products p ON oi.product_id = p.id
WHERE
	shop_id = :shop_id
GROUP BY
    t.shop_id, t.table_name, t.status;
    ');

    $stmt->bindParam(':shop_id', $shop_id);
    $stmt->execute();
    $mang = array();

    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        $id_shop = $row['id_shop'];
        $id_table = $row['id_table'];
        $table_name = $row['name_table'];
        $table_price = $row['total_table'];
        $status = $row['status'];
        array_push($mang, new Tables($id_shop, $id_table, $table_name, $table_price, $status));
    }

    echo json_encode($mang);
} catch (PDOException $pe) {
    die("Could not connect to the database $dbname :" . $pe->getMessage());
}

class Tables
{
    public $id_shop;
    public $id_table;
    public $table_name;
    public $table_price;
    public $status;

    public function __construct($id_shop, $id_table, $table_name, $table_price, $status)
    {
        $this->id_shop = $id_shop;
        $this->id_table = $id_table;
        $this->table_name = $table_name;
        $this->table_price = $table_price;
        $this->status = $status;
    }
}
