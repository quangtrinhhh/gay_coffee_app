<?php
require "../dbConnect.php";

try {

    $conn = new PDO("mysql:host=$host;dbname=$dbname", $username, $password);
    $stmt = $conn->prepare('SELECT * from ORDERS');
    $stmt->execute();
    $mang = array();

    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        $id = $row['id'];
        $dateTime = $row['dateTime'];
        $code = $row['code'];
        $table_id = $row['table_id'];
        $user_id = $row['user_id'];
        array_push($mang, new Orders($id, $dateTime, $code, $table_id, $user_id));
    }

    echo json_encode($mang);
} catch (PDOException $pe) {
    die("Could not connect to the database $dbname :" . $pe->getMessage());
}

class Orders
{
    public $id;
    public $dateTime;
    public $code;
    public $table_id;
    public $user_id;

    public function __construct($id, $dateTime, $code, $table_id, $user_id)
    {
        $this->id = $id;
        $this->dateTime = $dateTime;
        $this->code = $code;
        $this->table_id = $table_id;
        $this->user_id = $user_id;
    }
}
