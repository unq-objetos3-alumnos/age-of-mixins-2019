import org.scalatest.FlatSpec
import ar.edu.unq.obj3.aoe._

class AgeSpec extends FlatSpec {

  "Un Guerrero" should "ocasionar danio al atacar a otro guerrero mas debil" in {
    val guerrero1 = new Guerrero(100, 10, 9)
    val guerrero2 = new Guerrero(100, 12, 7)

    guerrero1.atacar(guerrero2)
    assert(guerrero2.energia == 97)
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
    assert(guerrero2.energia == 94)
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
    assert(muralla.energia == 5)
  }




}
