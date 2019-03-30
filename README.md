Age of Mixins
--

Muy sencillamente: se nos pide modelar un juego similar al Age of Empires donde tenemos unidades (guerreros) que pueden
atacar a otros guerreros y ser atacados.


#### Guerreros
Cuando un Guerrero ataca ocasiona un daño a la otra unidad igual a su `fuerza` menos la `defensa` del atacado. Al
dañarse una unidad disminuirá su energía.

Para modelar este comportamiento decidimos tener una clase `Guerrero` la cual será utilizada para instanciar Guerreros
que saben atacar, de momento, solamente a otros guerreros.


```scala
class Guerrero(var energia:Int, val fuerza:Int, val defensa:Int) {

  def atacar(otro: Guerrero) = {
     if (fuerza > otro.defensa) {
       otro.recibirDanio(fuerza - otro.defensa)
     }
  }
 
  def recibirDanio(danio: Int) = {
     this.energia = this.energia - danio
  }
   
}
```

#### Espadachin
Luego introdujimos al `Espadachin`. Un espadachín es un guerrero el cual usa un arma que modificara su ataque (al atacar)
multiplicando por cierto coeficiente.

En un primer momento decidimos extender la clase `Guerrero` para crear un `Espadachin` que redefiniese el método `atacar`.
Decidimos de momento no modelar el `Arma` como un objeto ya que solo nos importa su coeficiente y cómo afecta este
al espadachin (no obstante podríamos mas tarde refactorizar esto para introducir la clase `Arma` si es necesario)

```scala
class Espadachin(energia:Int, fuerza:Int, defensa:Int, val coeficienteArma:Int)
 extends Guerrero(energia, fuerza, defensa) {

  override def atacar(otro: Guerrero) = {
     if (fuerza * coeficienteArma > otro.defensa) {
       otro.recibirDanio(fuerza * coeficienteArma - otro.defensa)
     }
  }
   
}
```

Tanto `Espadachin` como `Guerrero` son polimórficos: ambos entienden el mensaje `atacar` pero responden cada uno con
implementaciones distintas.

Mmm... funcionar funcionó (los tests nos dieron verde), pero nos dimos cuenta casi inmediatamente que algo estaba mal.
Había mucha código repetido! Entonces cambiamos nuestra implementación:


```scala
class Guerrero(var energia:Int, val fuerza:Int, val defensa:Int) {

  def ataque:Int = fuerza

  def atacar(otro: Guerrero) = {
     if (ataque > otro.defensa) {
       otro.recibirDanio(ataque - otro.defensa)
     }
  }
 
  def recibirDanio(danio: Int) = {
     this.energia = this.energia - danio
  }
   
}

class Espadachin(energia:Int, fuerza:Int, defensa:Int, val coeficienteArma:Int)
 extends Guerrero(energia, fuerza, defensa) {

 override def ataque:Int = super.ataque * coeficienteArma
   
}
```

Ahora la lógica de cómo se realiza un ataque está solo contenida en Guerrero y no duplicada.

#### Murallas

El enunciado nos introduce otro requerimiento: Las `Murallas`. Una Muralla sabía defenderse (y podía recibir daño) pero
no atacaba. La solución natural para no reescribir el código de `recibirDanio` y la lógica de mantenimiento de la energía
fue la de introducir una superclase `Defensor` que tuviese a `Muralla` y a `Guerrero` como subclases directas.


```scala
class Defensor(var energia:Int, val defensa:Int) {
  def recibirDanio(danio: Int) = {
       this.energia = this.energia - danio
  }
}

class Guerrero(energia:Int, val fuerza:Int, defensa:Int) extends Defensor(energia, defensa) {

  def ataque:Int = fuerza

  def atacar(otro: Defensor) = {
     if (ataque > otro.defensa) {
       otro.recibirDanio(ataque - otro.defensa)
     }
  }

}

class Muralla(energia: Int, defensa:Int) extends Defensor(energia, defensa)
```

En este punto nos cuestionamos si de verdad tenía sentido tener una clase `Muralla`.  A simple vista no aporta ningún
comportamiento adicional. Por qué no podemos instanciar sencillamente un `Defensor` y decir que es una muralla?

```scala
   val muralla = new Defensor(1000, 500);
```

Esta es una de las cosas al estar sumamente atento al modelar con objetos: no es necesario que todas las entidades que
existen "en el mundo real" (el juego en este caso) existan como clases en nuestro modelo.

Justificamos la existencia de la muralla cambiando un poco el requerimiento: desde ahora la defensa de una `Muralla`
estará calculada como `100 / antigüedad de la muralla`.  Como la edad de la muralla puede ir cambiando con el tiempo, y
como quiero encapsular el cálculo de la defensa en la muralla misma, entonces me queda comodo no modelar mas la `defensa`
como un valor constante sino como un método que realice el cálculo.  Para los guerreros el cálculo será trivial.

```scala
abstract class Defensor(var energia:Int) {

  def defensa: Int
 
  def recibirDanio(danio: Int) = {
       this.energia = this.energia - danio
  }
}

class Guerrero(energia:Int, val fuerza:Int, _defensa:Int) extends Defensor(energia) {

  def ataque:Int = fuerza
 
  override def defensa:Int = _defensa

  def atacar(otro: Defensor) = {
     if (ataque > otro.defensa) {
       otro.recibirDanio(ataque - otro.defensa)
     }
  }

}

class Muralla(energia: Int, val antiguedad:Int) extends Defensor(energia) {

  override def defensa:Int = 100 / antiguedad
 
}
```

Hicimos luego la salvedad de que escribir esto `override def defensa:Int = _defensa` es análogo a escribir `override val defensa`
en el constructor.

> Gran paréntesis: En scala un atributo definido como `val` expone a los clientes del objeto un getter - no se accede a la
estado directamente -. Dicho getter es un método y puede ser visto como un caso particular de un método `def` sin parámetros.
(No así a la inversa ya que el contrato de `val` es más fuerte que el de `def` al asegurar que el valor devuelto no cambia).
Entonces, por lo dicho anteriormente, y gracias a la magia del *principio de substitución de Liskov* podemos overridear
un `def` sin parámetros con un `val`

#### Misil!
Luego complicamos el enunciado introduciendo al `Misil`. Un misíl puede atacar pero no recibir daño ni defenderse (nadie
puede pegarle al misíl!).  Entonces nos vimos tentados a introducir, al igual que lo hicimos con `Defensor`, una clase
abstracta `Atacante`.  El problema fue que ese diseño forzaba a que `Guerrero` tuviese dos superclases cosa que no es
posible en `scala` (y en muchos lenguajes) debido a las dificultades de implementación asociadas a la resolución de
conflictos.

La solución a la que llegamos fue introducir y utilizar Mixins. Creamos un mixin `Defensor` en el cual agrupamos todo
el comportamiento referido a defenderse y recibir daño.  Creamos otro mixin `Atacante` el cual agrupa la lógica de atacar.

Refactorizamos entonces todas las clases de nuestro código para componerse a través de la combinación de esos dos mixins.

