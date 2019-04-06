import org.scalatest.FlatSpec
import ar.edu.unq.obj3.aoe._

class AgeSpec extends FlatSpec {

  "Un Guerrero" should "ocasionar danio al atacar a otro guerrero mas debil" in {
    val guerrero1 = new Guerrero(100, 10, 9)
    val guerrero2 = new Guerrero(100, 12, 7)

    guerrero1.atacar(guerrero2)
    assert(guerrero2.energia == 97)
  }

  "Un Guerrero" should "no ocasionar danio al atacar a otro guerrero que este en otra posicion" in {
    val guerrero1 = new Guerrero(100, 10, 9)
    val guerrero2 = new Guerrero(100, 12, 7)
    guerrero2.posicion = 4

    guerrero1.atacar(guerrero2)
    assert(guerrero2.energia == 100) //no le pasó nada!
  }

  "Un Guerrero" should "no ocasionar danio al atacar a otro guerrero con mas defensa que su fuerza" in {
    val guerrero1 = new Guerrero(100, 10, 9)
    val guerrero2 = new Guerrero(100, 12, 11)

    guerrero1.atacar(guerrero2)
    assert(guerrero2.energia == 100) //no le pasó nada!
  }

  "Un Espadachin" should "ocasionar danio producto de fuerza * su arma" in {
    val espadachin = new Espadachin(100, 10, 9,2)
    val guerrero2 = new Guerrero(100, 12, 7)

    espadachin.atacar(guerrero2)
    assert(guerrero2.energia == 87)
  }

  "Un Guerrero" should "ocasionar danio a una Muralla" in {
    val guerrero1 = new Guerrero(100, 10, 9)
    val muralla = new Muralla(1000, 5)

    guerrero1.atacar(muralla)
    assert(muralla.energia == 995)
  }

  "Un Misil" should "ocasionar danio a una Muralla" in {
    val misil = new Misil()
    val muralla = new Muralla(1000, 5)

    misil.atacar(muralla)
    assert(muralla.energia <= 0)
  }


  "Un GuerreroCansable" should "ocasionar danio las primeras 3 veces, y despues no hacer nada porque se canso" in {
    val guerrero1 = new Guerrero(100, 10, 9) with AtacanteQueSeCansa
    val muralla = new Muralla(1000, 5)

    guerrero1.atacar(muralla)
    assert(muralla.energia == 995)
    guerrero1.atacar(muralla)
    assert(muralla.energia == 990)
    guerrero1.atacar(muralla)
    assert(muralla.energia == 985)

    guerrero1.atacar(muralla)
    assert(muralla.energia == 985) //se canso!
    assert(guerrero1.estaCansado)
  }

  "Un AtancanteX2" should "ocasionar danio por dos ataques" in {
    val guerreroX2 = new Guerrero(100, 10, 9) with AtacanteX2
    val muralla = new Muralla(1000, 5)

    guerreroX2.atacar(muralla)
    assert(muralla.energia == 990) //ataco dos veces!!
  }

  "Un atacante que se cansa y depsues ataca x2" should "al atacar dos veces pegar 4" in {
    val guerrero = new Guerrero(100, 10, 9)
      with AtacanteX2
      with AtacanteQueSeCansa
    val muralla = new Muralla(1000, 5)

    guerrero.atacar(muralla)
    assert(muralla.energia == 990) //ataco dos veces!! y no se canso
    assert(! guerrero.estaCansado)

    guerrero.atacar(muralla)
    assert(muralla.energia == 980) //ataco dos veces!! y no se canso
    assert(! guerrero.estaCansado)
  }

  "Un atacante que ataca x2 y despues se cansa" should "al atacar dos veces pegar 3" in {
    val guerrero = new Guerrero(100, 10, 9)
      with AtacanteX2
      with AtacanteQueSeCansa
    val muralla = new Muralla(1000, 5)

    guerrero.atacar(muralla)
    assert(muralla.energia == 990) //ataco dos veces!! y no se canso
    assert(! guerrero.estaCansado)

    guerrero.atacar(muralla)
    assert(muralla.energia == 980) //ataco dos veces!! y no se canso
    assert(! guerrero.estaCansado)
  }


  "Un GuerreroPolenta" should "atacar con 10 veces mas poder que su fuerza base" in {
    val guerreroPolenta = new Guerrero(100, 10, 9) with AtacantePolenta
    val guerrero2 = new Guerrero(100, 12, 0)

    guerreroPolenta.atacar(guerrero2)
    assert(guerrero2.energia == 0)
  }



}
