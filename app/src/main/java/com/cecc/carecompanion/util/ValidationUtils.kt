package com.cecc.carecompanion.util

object ValidationUtils {

    data class ValidationResult(
        val isValid: Boolean,
        val errorMessage: String? = null
    )

    fun validateParticipantId(pid: String): ValidationResult {
        return when {
            pid.isBlank() -> ValidationResult(false, "Participant ID is required")
            pid.length < 3 -> ValidationResult(false, "Participant ID must be at least 3 characters")
            !pid.matches(Regex("^[A-Za-z0-9_-]+$")) -> ValidationResult(false, "Participant ID can only contain letters, numbers, hyphens, and underscores")
            else -> ValidationResult(true)
        }
    }

    fun validateAge(age: String): ValidationResult {
        return try {
            val ageNum = age.toInt()
            when {
                ageNum < 1 -> ValidationResult(false, "Age must be greater than 0")
                ageNum > 120 -> ValidationResult(false, "Age must be less than 120")
                else -> ValidationResult(true)
            }
        } catch (e: NumberFormatException) {
            ValidationResult(false, "Please enter a valid age")
        }
    }

    fun validatePhone(phone: String): ValidationResult {
        return when {
            phone.isBlank() -> ValidationResult(true) // Phone is optional
            phone.length < 10 -> ValidationResult(false, "Phone number must be at least 10 digits")
            !phone.matches(Regex("^[0-9+\\-\\s()]+$")) -> ValidationResult(false, "Phone number contains invalid characters")
            else -> ValidationResult(true)
        }
    }

    fun validateBloodPressure(systolic: String, diastolic: String): ValidationResult {
        return try {
            val sys = systolic.toInt()
            val dia = diastolic.toInt()

            when {
                sys < 50 || sys > 300 -> ValidationResult(false, "Systolic BP must be between 50-300")
                dia < 30 || dia > 200 -> ValidationResult(false, "Diastolic BP must be between 30-200")
                sys <= dia -> ValidationResult(false, "Systolic BP must be higher than diastolic BP")
                else -> ValidationResult(true)
            }
        } catch (e: NumberFormatException) {
            ValidationResult(false, "Please enter valid blood pressure values")
        }
    }

    fun validateHeight(height: String): ValidationResult {
        return try {
            val heightNum = height.toFloat()
            when {
                heightNum < 50 -> ValidationResult(false, "Height must be at least 50 cm")
                heightNum > 250 -> ValidationResult(false, "Height must be less than 250 cm")
                else -> ValidationResult(true)
            }
        } catch (e: NumberFormatException) {
            ValidationResult(false, "Please enter a valid height in cm")
        }
    }

    fun validateWeight(weight: String): ValidationResult {
        return try {
            val weightNum = weight.toFloat()
            when {
                weightNum < 10 -> ValidationResult(false, "Weight must be at least 10 kg")
                weightNum > 300 -> ValidationResult(false, "Weight must be less than 300 kg")
                else -> ValidationResult(true)
            }
        } catch (e: NumberFormatException) {
            ValidationResult(false, "Please enter a valid weight in kg")
        }
    }

    fun validateIncome(income: String): ValidationResult {
        return try {
            val incomeNum = income.toInt()
            when {
                incomeNum < 0 -> ValidationResult(false, "Income cannot be negative")
                incomeNum > 1000000 -> ValidationResult(false, "Income seems unusually high")
                else -> ValidationResult(true)
            }
        } catch (e: NumberFormatException) {
            ValidationResult(false, "Please enter a valid income amount")
        }
    }

    fun validateHouseholdSize(size: String): ValidationResult {
        return try {
            val sizeNum = size.toInt()
            when {
                sizeNum < 1 -> ValidationResult(false, "Household must have at least 1 member")
                sizeNum > 50 -> ValidationResult(false, "Household size seems unusually large")
                else -> ValidationResult(true)
            }
        } catch (e: NumberFormatException) {
            ValidationResult(false, "Please enter a valid household size")
        }
    }

    fun validateRequiredText(text: String, fieldName: String): ValidationResult {
        return when {
            text.isBlank() -> ValidationResult(false, "$fieldName is required")
            text.length < 2 -> ValidationResult(false, "$fieldName must be at least 2 characters")
            else -> ValidationResult(true)
        }
    }

    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult(true) // Email is optional
            !email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) ->
                ValidationResult(false, "Please enter a valid email address")
            else -> ValidationResult(true)
        }
    }

    fun validatePin(pin: String): ValidationResult {
        return when {
            pin.isBlank() -> ValidationResult(false, "PIN is required")
            pin.length < 4 -> ValidationResult(false, "PIN must be at least 4 digits")
            pin.length > 8 -> ValidationResult(false, "PIN must be no more than 8 digits")
            !pin.matches(Regex("^[0-9]+$")) -> ValidationResult(false, "PIN must contain only numbers")
            else -> ValidationResult(true)
        }
    }

    fun validateFormData(formData: Map<String, String>): List<String> {
        val errors = mutableListOf<String>()

        // Validate participant ID
        validateParticipantId(formData["pid"] ?: "").takeIf { !it.isValid }
            ?.errorMessage?.let { errors.add(it) }

        // Validate age if provided
        if (formData.containsKey("age") && formData["age"]?.isNotBlank() == true) {
            validateAge(formData["age"]!!).takeIf { !it.isValid }
                ?.errorMessage?.let { errors.add(it) }
        }

        // Validate phone if provided
        if (formData.containsKey("phone") && formData["phone"]?.isNotBlank() == true) {
            validatePhone(formData["phone"]!!).takeIf { !it.isValid }
                ?.errorMessage?.let { errors.add(it) }
        }

        return errors
    }
}
