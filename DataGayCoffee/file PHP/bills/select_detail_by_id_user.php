<?php
require "../dbConnect.php";

try {
    $user_id_account = $_POST['user_id'];
    $date_account = $_POST['date'];

    $conn = new PDO("mysql:host=$host;dbname=$dbname", $username, $password);
    $stmt = $conn->prepare('SELECT DATE(`bills`.`dateTime_end`) as `date`, `tables`.`table_name`, SUM(`bills`.`total_price`) as `total_price`, COUNT(`bills`.`user_id`) as `quantity`
    FROM `bills`
    JOIN `tables`
    ON `bills`.`table_id` = `tables`.`id`
    WHERE DATE(`bills`.`dateTime_end`) = :date_account
    AND `bills`.`user_id` = :user_id_account
    GROUP BY DATE(`bills`.`dateTime_end`), `tables`.`table_name`');

    $stmt->bindParam(':user_id_account', $user_id_account);
    $stmt->bindParam(':date_account', $date_account);

    $stmt->execute();
    $billArray = array();

    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        $quantity = $row['quantity'];
        $total_price = $row['total_price'];
        $name = $row['table_name'];
        $dateTime_end = $row['date'];
        array_push($billArray, new Bill_ITEMS($quantity, $total_price, $name, $dateTime_end));
    }

    echo json_encode($billArray);
} catch (PDOException $pe) {
    die("Could not connect to the database $dbname :" . $pe->getMessage());
}

class Bill_ITEMS
{
    public $quantity;
    public $total_price;
    public $name;
    public $dateTime_end;

    public function __construct($quantity, $total_price, $name, $dateTime_end)
    {
        $this->quantity = $quantity;
        $this->total_price = $total_price;
        $this->name = $name;
        $this->dateTime_end = $dateTime_end;
    }
}
