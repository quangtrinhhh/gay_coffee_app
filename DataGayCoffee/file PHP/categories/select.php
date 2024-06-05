<?php
require "../dbConnect.php";

try {

    $conn = new PDO("mysql:host=$host;dbname=$dbname", $username, $password);
    $stmt = $conn->prepare('SELECT * from CATEGORIES');
    $stmt->execute();
    $mang = array();

    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        $id = $row['id'];
        $name = $row['name'];
        array_push($mang, new Categories($id, $name));
    }

    echo json_encode($mang);
} catch (PDOException $pe) {
    die("Could not connect to the database $dbname :" . $pe->getMessage());
}

class Categories
{
    public $id;
    public $name;

    public function __construct($id, $name)
    {
        $this->id = $id;
        $this->name = $name;
    }
}
