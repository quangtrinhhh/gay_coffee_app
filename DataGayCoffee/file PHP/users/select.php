<?php
require "../dbConnect.php";

try {

    $conn = new PDO("mysql:host=$host;dbname=$dbname", $username, $password);
    $stmt = $conn->prepare('SELECT shops.id AS shop_id, shops.user_id, shops.name AS restaurant_name, users.name AS user_name, users.password, users.role 
    FROM shops
    INNER JOIN users ON shops.user_id = users.id;');
    $stmt->execute();
    $mang = array();

    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        $shop_id = $row['shop_id'];
        $user_id = $row['user_id'];
        $name = $row['restaurant_name'];
        $user_name = $row['user_name'];
        $password = $row['password'];
        $role = $row['role'];
        array_push($mang, new Users($shop_id, $user_id, $name, $user_name, $password, $role));
    }

    echo json_encode($mang);
} catch (PDOException $pe) {
    die("Could not connect to the database $dbname :" . $pe->getMessage());
}

class Users
{
    public $shop_id;
    public $user_id;
    public $name;
    public $user_name;
    public $password;
    public $role;

    public function __construct($shop_id, $user_id, $name, $user_name, $password, $role)
    {
        $this->shop_id = $shop_id;
        $this->user_id = $user_id;
        $this->name = $name;
        $this->user_name = $user_name;
        $this->password = $password;
        $this->role = $role;
    }
}
