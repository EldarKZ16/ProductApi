package repository

import cats.effect.unsafe.implicits.global
import model.ProductEntity
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class InMemoryProductRepoImplSpec
  extends AnyFlatSpec
    with Matchers {

  behavior of "InMemoryProductRepoImpl"

  val inMemoryProductRepo = new InMemoryProductRepoImpl

  val vendor = "Apple"
  val testProductId1 = "random-product-1"
  val testProduct1: ProductEntity = ProductEntity("Mac", vendor, 100)

  val testProductId2 = "random-product-2"
  val testProduct2: ProductEntity = ProductEntity("MacPro", vendor, 10000)

  val nonExistingProductId = "non-existing-product"

  it should "save the product entity and retrieve it by id" in {
    val result = inMemoryProductRepo.add(testProductId1, testProduct1)
    result.unsafeRunSync() shouldBe testProductId1

    val product = inMemoryProductRepo.get(testProductId1)
    product.unsafeRunSync() shouldBe Some(testProduct1)
  }

  it should "return an empty product" in {
    val product = inMemoryProductRepo.get("nonExistingProductId")
    product.unsafeRunSync() shouldBe None
  }

  it should "retrieve the products by vendor name" in {
    inMemoryProductRepo.add(testProductId2, testProduct2).unsafeRunSync()

    val result = inMemoryProductRepo.getByVendor(vendor)
    result.unsafeRunSync() shouldBe List(testProduct1, testProduct2)
  }

  it should "delete the product entity by id" in {
    val product = inMemoryProductRepo.delete(testProductId1)
    product.unsafeRunSync() shouldBe Right(())
  }

}
