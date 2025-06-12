package io.rwc.streamwise.flows

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class FixedTest {
    
    @Test
    fun `test valueOn with matching date`() {
        val date = LocalDate(2023, 5, 15)
        val amount = BigDecimal.fromInt(1000)
        val fixed = Fixed(date, amount)
        
        // Test with the same date
        assertEquals(amount, fixed.valueOn(date))
    }
    
    @Test
    fun `test valueOn with non-matching date`() {
        val date = LocalDate(2023, 5, 15)
        val amount = BigDecimal.fromInt(1000)
        val fixed = Fixed(date, amount)
        
        // Test with a different date
        val differentDate = LocalDate(2023, 5, 16)
        assertEquals(BigDecimal.ZERO, fixed.valueOn(differentDate))
    }
    
    @Test
    fun `test valueOn with negative amount`() {
        val date = LocalDate(2023, 5, 15)
        val amount = BigDecimal.fromInt(-500)
        val fixed = Fixed(date, amount)
        
        // Test with the same date
        assertEquals(amount, fixed.valueOn(date))
        
        // Test with a different date
        val differentDate = LocalDate(2023, 5, 16)
        assertEquals(BigDecimal.ZERO, fixed.valueOn(differentDate))
    }
    
    @Test
    fun `test valueOn with zero amount`() {
        val date = LocalDate(2023, 5, 15)
        val amount = BigDecimal.ZERO
        val fixed = Fixed(date, amount)
        
        // Test with the same date
        assertEquals(amount, fixed.valueOn(date))
        
        // Test with a different date
        val differentDate = LocalDate(2023, 5, 16)
        assertEquals(BigDecimal.ZERO, fixed.valueOn(differentDate))
    }
    
    @Test
    fun `test data class equality`() {
        val date = LocalDate(2023, 5, 15)
        val amount = BigDecimal.fromInt(1000)
        val fixed1 = Fixed(date, amount)
        val fixed2 = Fixed(date, amount)
        
        // Test data class equality
        assertEquals(fixed1, fixed2)
    }
}