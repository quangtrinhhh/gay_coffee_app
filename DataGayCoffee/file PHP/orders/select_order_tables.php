<?php
require "../dbConnect.php";

try {

    $conn = new PDO("mysql:host=$host;dbname=$dbname", $username, $password);
    $stmt = $conn->prepare('SELECT orders.id , orders.table_id, orders.user_id , tables.table_name FROM tables
JOIN orders on orders.table_id = tables.id;');
    $stmt->execute();
    $mang = array();

    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        $id = $row['id'];
        $table_id = $row['table_id'];
        $user_id = $row['user_id'];
        $table_name = $row['table_name'];


        array_push($mang, new Orders($id, $table_id, $user_id, $table_name));
    }

    echo json_encode($mang);
} catch (PDOException $pe) {
    die("Could not connect to the database $dbname :" . $pe->getMessage());
}

class Orders
{
    public $user_id;
    public $table_id;
    public $table_name;
    public $id;

    public function __construct($id, $table_id, $user_id, $table_name)
    {
        $this->id = $id;
        $this->user_id = $user_id;
        $this->table_id = $table_id;
        $this->table_name = $table_name;

    }
}
