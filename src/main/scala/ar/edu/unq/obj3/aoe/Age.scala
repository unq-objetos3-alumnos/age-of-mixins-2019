package ar.edu.unq.obj3.aoe

/**
  * Mixin Defensor
  */
trait Defensor {

  var energia:Int // variable abstracta

  def defensa:Int // metodo abstracto

  def recibirDanio(danio: Int) = {
    this.energia = this.energia - danio
  }

}

/**
  * Mixin Atacante
  */
trait Atacante {

  def ataque:Int // metodo abstracto

  def atacar(otro: Defensor) = {
    if (ataque > otro.defensa) {
      otro.recibirDanio(ataque - otro.defensa)
    }
  }

}


/**
  * La clase Guerrero se compone (se mixin..ea?) con los mixins Defensor y Atacante por lo que debe
  * proveer implementaciones a `var energia`, `def defensa`, `def ataque`
  */
class Guerrero(var energia:Int, val fuerza:Int, override val defensa:Int)
  extends Defensor with Atacante {

  // el `override val defensa` del constructor es un syntax sugar de esto de abajo:
  // override def defensa: Int = variableDefensaDelConstructor (necesita ser renombrada si vamos por este camino)

  def ataque= this.fuerza;

}

class Espadachin(energia:Int, fuerza:Int, defensa:Int, val coefArma:Int)
  extends Guerrero(energia, fuerza, defensa) {

  override def ataque = super.ataque * coefArma

}

/**
  * La clase Muralla solo se compone con el mixin Defensor
  */
class Muralla(var energia: Int, antiguedad:Int)
  extends Defensor {

  override def defensa = 10 - antiguedad

}

/**
  * La clase Misil solo se compone con el mixin Atacante
  */
class Misil extends Atacante {

  def ataque = 1000000

}