package ar.edu.unq.obj3.aoe


class Unidad(var posicion:Int) {

  def mover(nuevaPosicion:Int): Unit = {
    posicion = nuevaPosicion
  }

  def estasEnElMismoLugar(otra:Unidad) = {
    otra.posicion == posicion
  }

}

/**
  * Mixin Defensor (aqui el extends Unidad significa que el trait Defensor solo puede ser aplicado a clases
  * que sean subclases de Unidad)
  */
trait Defensor extends Unidad {

  var energia:Int // variable abstracta

  def defensa:Int // metodo abstracto

  def recibirDanio(danio: Int) = {
    this.energia = this.energia - danio
  }

}


/**
  * Mixin Atacanten (aqui el extends Unidad significa que el trait Atacante solo puede ser aplicado a clases
  * que sean subclases de Unidad)
  */
trait Atacante extends Unidad {

  def ataque:Int // metodo abstracto

  def atacar(otro: Defensor) = {
    if (this.estasEnElMismoLugar(otro) && ataque > otro.defensa) {
      otro.recibirDanio(ataque - otro.defensa)
    }
  }

}


/**
  * La clase Guerrero se compone (se mixin..ea?) con los mixins Defensor y Atacante por lo que debe
  * proveer implementaciones a `var energia`, `def defensa`, `def ataque`
  */
class Guerrero(var energia:Int, val fuerza:Int, override val defensa:Int)
  extends Unidad(0) with Defensor with Atacante {

  // el `override val defensa` del constructor es un syntax sugar de esto de abajo:
  // override def defensa: Int = variableDefensaDelConstructor (necesita ser renombrada si vamos por este camino)

  def ataque= this.fuerza;

}

// No necesito esas clases, siempre puedo hacer `new Guerrero(energia, fuerza, defensa) with AtacanteQueSeCansa`
//class GuerreroCansable(energia:Int, fuerza:Int, defensa:Int)
//  extends Guerrero(energia, fuerza, defensa)
//    with AtacanteQueSeCansa

//class EspadachinCansable(energia:Int, fuerza:Int, defensa:Int, coefArma:Int)
//  extends Espadachin(energia, fuerza, defensa, coefArma)
//    with AtacanteQueSeCansa


class Espadachin(energia:Int, fuerza:Int, defensa:Int, val coefArma:Int)
  extends Guerrero(energia, fuerza, defensa) {

  override def ataque = super.ataque * coefArma

}

/**
  * La clase Muralla solo se compone con el mixin Defensor
  */
class Muralla(var energia: Int, antiguedad:Int)
  extends Unidad(0) with Defensor {

  override def defensa = 10 - antiguedad

}

/**
  * La clase Misil solo se compone con el mixin Atacante
  */
class Misil extends Unidad(0) with Atacante {

  def ataque = 1000000

}


trait AtacanteQueSeCansa extends Atacante {

  var vecesQueAtaque = 0

  def estaCansado: Boolean = this.vecesQueAtaque >= 3

  override def atacar(otro:Defensor) = {
    if (!estaCansado) {
      super.atacar(otro)
      this.vecesQueAtaque += 1
    }
  }

}

trait AtacanteX2 extends Atacante {

  override def atacar(otro:Defensor): Unit = {
    super.atacar(otro)
    super.atacar(otro)
  }

}

trait AtacantePolenta extends Atacante {

  //Necesito declarar el metodo como `abstract override` ya ataque en el mixin "padre" es abstracto
  //(pero eso nos importa porque gracias a la linearización no puedo saber en este momento exactamente
  // quién es super.
  abstract override def ataque = 10 * super.ataque

}