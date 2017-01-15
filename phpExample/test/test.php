<?php

Class Yii {
    /** @var App  */
    public static $app;

    /**
     * @param string $class
     * @return  mixed
     */
    public function get(string $class)
    {

    }
}

class App {
    public function getFoo()
    {

    }
}
class View {
    public static function factory(string $file)
    {

    }
}

$app = new Yii();
$app->get(App::class)->getFoo();

View::factory('view/text');
