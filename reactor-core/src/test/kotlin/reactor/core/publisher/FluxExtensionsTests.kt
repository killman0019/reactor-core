/*
 * Copyright (c) 2011-2017 Pivotal Software Inc, All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package reactor.core.publisher

import org.junit.Assert
import org.junit.Test
import reactor.test.StepVerifier
import reactor.test.verifyError
import java.io.IOException

class FluxExtensionsTests {

    @Test
    fun `Iterator to Flux`() {
        StepVerifier
                .create(listOf("foo", "bar", "baz").listIterator().toFlux())
                .expectNext("foo", "bar", "baz")
                .verifyComplete()
    }

    @Test
    fun `Iterable to Flux`() {
        StepVerifier
                .create(listOf("foo", "bar", "baz").asIterable().toFlux())
                .expectNext("foo", "bar", "baz")
                .verifyComplete()
    }

    @Test
    fun `Sequence to Flux`() {
        StepVerifier
                .create(listOf("foo", "bar", "baz").asSequence().toFlux())
                .expectNext("foo", "bar", "baz")
                .verifyComplete()
    }

    @Test
    fun `Stream to Flux`() {
        StepVerifier
                .create(listOf("foo", "bar", "baz").stream().toFlux())
                .expectNext("foo", "bar", "baz")
                .verifyComplete()
    }

    @Test
    fun `ByteArray to Flux`() {
        StepVerifier
                .create(byteArrayOf(Byte.MAX_VALUE, Byte.MIN_VALUE, Byte.MAX_VALUE).toFlux())
                .expectNext(Byte.MAX_VALUE, Byte.MIN_VALUE, Byte.MAX_VALUE)
                .verifyComplete()
    }

    @Test
    fun `ShortArray to Flux`() {
        StepVerifier
                .create(shortArrayOf(1, 2, 3).toFlux())
                .expectNext(1, 2, 3)
                .verifyComplete()
    }

    @Test
    fun `IntArray to Flux`() {
        StepVerifier
                .create(intArrayOf(1, 2, 3).toFlux())
                .expectNext(1, 2, 3)
                .verifyComplete()
    }

    @Test
    fun `LongArray to Flux`() {
        StepVerifier
                .create(longArrayOf(1, 2, 3).toFlux())
                .expectNext(1, 2, 3)
                .verifyComplete()
    }

    @Test
    fun `FloatArray to Flux`() {
        StepVerifier
                .create(floatArrayOf(1.0F, 2.0F, 3.0F).toFlux())
                .expectNext(1.0F, 2.0F, 3.0F)
                .verifyComplete()
    }

    @Test
    fun `DoubleArray to Flux`() {
        StepVerifier
                .create(doubleArrayOf(1.0, 2.0, 3.0).toFlux())
                .expectNext(1.0, 2.0, 3.0)
                .verifyComplete()
    }

    @Test
    fun `BooleanArray to Flux`() {
        StepVerifier
                .create(booleanArrayOf(true, false, true).toFlux())
                .expectNext(true, false, true)
                .verifyComplete()
    }


    @Test
    fun `Throwable to Flux`() {
        StepVerifier
                .create(IllegalStateException().toFlux<Any>())
                .verifyError(IllegalStateException::class)
    }

    @Test
    fun `cast() with generic parameter`() {
        val fluxOfAny: Flux<Any> = Flux.just("foo")
        StepVerifier
                .create(fluxOfAny.cast<String>())
                .expectNext("foo").verifyComplete()
    }

    @Test
    fun doOnError() {
        val fluxOnError: Flux<Any> = IllegalStateException().toFlux()
        var invoked = false
        fluxOnError.doOnError(IllegalStateException::class, {
            invoked = true
        }).subscribe()
        Assert.assertTrue(invoked)
    }

    @Test
    fun onErrorMap() {
        StepVerifier
                .create(IOException()
                        .toFlux<Any>()
                        .onErrorMap(IOException::class, ::IllegalStateException))
                .verifyError<IllegalStateException>()
    }

    @Test
    fun `ofType() with generic parameter`() {
        StepVerifier
                .create(arrayOf("foo", 1).toFlux().ofType<String>())
                .expectNext("foo").verifyComplete()
    }

    @Test
    fun onErrorResume() {
        StepVerifier
                .create(IOException()
                        .toFlux<String>()
                        .onErrorResume(IOException::class, { "foo".toMono() }))
                .expectNext("foo")
                .verifyComplete()
    }

    @Test
    fun onErrorReturn() {
        StepVerifier
                .create(IOException()
                    .toFlux<String>()
                    .onErrorReturn(IOException::class, "foo"))
                .expectNext("foo")
                .verifyComplete()
    }

}