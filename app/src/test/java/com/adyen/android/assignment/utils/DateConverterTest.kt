package com.adyen.android.assignment.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DateConverterTest {

    @Test
    fun `empty date value returns empty string`(){
        val result = DateConverter.formatDateToDdMmYyyy("")
        assertThat(result).isEmpty()
    }

    @Test
    fun `date value is not in yyyy-MM-dd format returns empty string`() {
        val result = DateConverter.formatDateToDdMmYyyy("2012-01-")
        assertThat(result).isEmpty()
    }

    /**
     * test that a valid date value format yyyy-MM-dd
     * returns dd/MM/yyyy
     */

    @Test
    fun `date value is in yyyy-MM-dd format returns correct result format`(){
        val result = DateConverter.formatDateToDdMmYyyy("2012-01-09")
        assertThat(result).isEqualTo("09/01/2012")
    }


}