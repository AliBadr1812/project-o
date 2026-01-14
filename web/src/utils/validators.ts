// src/utils/validators.ts

import { VALIDATION_RULES } from './constants';
import { formatDate } from './formatters';

/**
 * Validation error type
 */
export interface ValidationError {
  field: string;
  message: string;
}

/**
 * Validation rule type
 */
export interface ValidationRule {
  required?: boolean;
  pattern?: RegExp;
  min?: number;
  max?: number;
  minLength?: number;
  maxLength?: number;
  custom?: (value: unknown, data?: Record<string, unknown>) => string | null;
  message?: string;
}

/**
 * Validate email address
 */
export function validateEmail(email: string | null | undefined): string | null {
  if (!email) return 'Email is required';
  if (!VALIDATION_RULES.EMAIL.test(email.trim())) {
    return 'Please enter a valid email address';
  }
  return null;
}

/**
 * Validate password
 */
export function validatePassword(password: string | null | undefined): string | null {
  if (!password) return 'Password is required';

  const trimmedPassword = password.trim();

  if (trimmedPassword.length < 8) {
    return 'Password must be at least 8 characters long';
  }
  if (!/[A-Z]/.test(trimmedPassword)) {
    return 'Password must contain at least one uppercase letter';
  }
  if (!/[a-z]/.test(trimmedPassword)) {
    return 'Password must contain at least one lowercase letter';
  }
  if (!/\d/.test(trimmedPassword)) {
    return 'Password must contain at least one number';
  }
  if (!/[!@#$%^&*(),.?":{}|<>]/.test(trimmedPassword)) {
    return 'Password must contain at least one special character';
  }
  return null;
}

/**
 * Validate phone number
 */
export function validatePhone(phone: string | null | undefined): string | null {
  if (!phone) return 'Phone number is required';
  if (!VALIDATION_RULES.PHONE.test(phone.replace(/\D/g, ''))) {
    return 'Please enter a valid phone number';
  }
  return null;
}

/**
 * Validate required field
 */
export function validateRequired(
  value: unknown,
  fieldName: string = 'This field'
): string | null {
  if (value === undefined || value === null || value === '') {
    return `${fieldName} is required`;
  }
  if (Array.isArray(value) && value.length === 0) {
    return `${fieldName} is required`;
  }
  if (typeof value === 'object' && Object.keys(value).length === 0) {
    return `${fieldName} is required`;
  }
  return null;
}

/**
 * Validate number range
 */
export function validateNumber(
  value: number | string | null | undefined,
  options: {
    min?: number;
    max?: number;
    required?: boolean;
    fieldName?: string;
  } = {}
): string | null {
  const { min, max, required = true, fieldName = 'Value' } = options;

  if (required && (value == null || value === '')) {
    return `${fieldName} is required`;
  }

  if (value == null || value === '') return null;

  const num = Number(value);
  if (isNaN(num)) return `${fieldName} must be a valid number`;

  if (min !== undefined && num < min) {
    return `${fieldName} must be at least ${min}`;
  }

  if (max !== undefined && num > max) {
    return `${fieldName} must be at most ${max}`;
  }

  return null;
}

/**
 * Validate URL
 */
export function validateUrl(url: string | null | undefined): string | null {
  if (!url) return null; // URL is optional

  try {
    new URL(url);
    return null;
  } catch {
    return 'Please enter a valid URL';
  }
}

/**
 * Validate price/currency amount
 */
export function validatePrice(
  price: number | string | null | undefined
): string | null {
  if (!price && price !== 0) return 'Price is required';

  const priceStr = String(price);
  if (!VALIDATION_RULES.PRICE.test(priceStr)) {
    return 'Please enter a valid price (e.g., 10.99)';
  }

  const num = Number(price);
  if (num < 0) return 'Price cannot be negative';

  return null;
}

/**
 * Validate date
 */
export function validateDate(
  date: string | Date | null | undefined,
  options: {
    minDate?: string | Date;
    maxDate?: string | Date;
    required?: boolean;
    fieldName?: string;
  } = {}
): string | null {
  const { minDate, maxDate, required = true, fieldName = 'Date' } = options;

  if (required && !date) return `${fieldName} is required`;
  if (!date) return null;

  const dateObj = new Date(date);
  if (isNaN(dateObj.getTime())) return `${fieldName} is invalid`;

  if (minDate) {
    const minDateObj = new Date(minDate);
    if (dateObj < minDateObj) {
      return `${fieldName} cannot be earlier than ${formatDate(minDate)}`;
    }
  }

  if (maxDate) {
    const maxDateObj = new Date(maxDate);
    if (dateObj > maxDateObj) {
      return `${fieldName} cannot be later than ${formatDate(maxDate)}`;
    }
  }

  return null;
}

/**
 * Validate file upload
 */
export function validateFile(
  file: File | null | undefined,
  options: {
    allowedTypes?: string[];
    maxSize?: number; // in bytes
    required?: boolean;
  } = {}
): string | null {
  const { allowedTypes, maxSize, required = true } = options;

  if (required && !file) return 'File is required';
  if (!file) return null;

  if (maxSize && file.size > maxSize) {
    return `File size must be less than ${Math.round(maxSize / 1024 / 1024)}MB`;
  }

  if (allowedTypes && allowedTypes.length > 0) {
    const fileType = file.type;
    const fileExtension = file.name.split('.').pop()?.toLowerCase();

    const isTypeValid = allowedTypes.some(type => {
      if (type.startsWith('.')) {
        return `.${fileExtension}` === type;
      }
      return fileType.startsWith(type.split('/')[0]);
    });

    if (!isTypeValid) {
      return `File type must be: ${allowedTypes.join(', ')}`;
    }
  }

  return null;
}

/**
 * Validate array length
 */
export function validateArray(
  array: unknown[] | null | undefined,
  options: {
    minLength?: number;
    maxLength?: number;
    required?: boolean;
    fieldName?: string;
  } = {}
): string | null {
  const { minLength, maxLength, required = true, fieldName = 'Items' } = options;

  if (required && (!array || array.length === 0)) {
    return `${fieldName} are required`;
  }

  if (!array) return null;

  if (minLength !== undefined && array.length < minLength) {
    return `${fieldName} must have at least ${minLength} item(s)`;
  }

  if (maxLength !== undefined && array.length > maxLength) {
    return `${fieldName} cannot have more than ${maxLength} item(s)`;
  }

  return null;
}

/**
 * Validate credit card number
 */
export function validateCreditCard(cardNumber: string | null | undefined): string | null {
  if (!cardNumber) return 'Card number is required';

  // Remove all non-digits
  const cleaned = cardNumber.replace(/\D/g, '');

  // Check if it's a valid length (13-19 digits)
  if (cleaned.length < 13 || cleaned.length > 19) {
    return 'Please enter a valid card number';
  }

  // Luhn algorithm check
  let sum = 0;
  let isEven = false;

  for (let i = cleaned.length - 1; i >= 0; i--) {
    let digit = parseInt(cleaned.charAt(i), 10);

    if (isEven) {
      digit *= 2;
      if (digit > 9) digit -= 9;
    }

    sum += digit;
    isEven = !isEven;
  }

  if (sum % 10 !== 0) {
    return 'Please enter a valid card number';
  }

  return null;
}

/**
 * Composite validator for form fields
 */
export function validateForm<T extends Record<string, unknown>>(
  data: T,
  rules: Record<string, ValidationRule>
): Record<string, string> {
  const errors: Record<string, string> = {};

  Object.keys(rules).forEach(field => {
    const value = data[field];
    const fieldRules = rules[field];

    // Required validation
    if (fieldRules.required) {
      const requiredError = validateRequired(value, field);
      if (requiredError) {
        errors[field] = fieldRules.message || requiredError;
        return;
      }
    }

    // Pattern validation
    if (value && fieldRules.pattern && !fieldRules.pattern.test(String(value))) {
      errors[field] = fieldRules.message || `${field} is invalid`;
      return;
    }

    // Min length validation
    if (value && fieldRules.minLength && String(value).length < fieldRules.minLength) {
      errors[field] = fieldRules.message ||
        `${field} must be at least ${fieldRules.minLength} characters`;
      return;
    }

    // Max length validation
    if (value && fieldRules.maxLength && String(value).length > fieldRules.maxLength) {
      errors[field] = fieldRules.message ||
        `${field} must be at most ${fieldRules.maxLength} characters`;
      return;
    }

    // Min value validation
    if (value !== null && value !== undefined && fieldRules.min !== undefined) {
      const num = Number(value);
      if (!isNaN(num) && num < fieldRules.min) {
        errors[field] = fieldRules.message ||
          `${field} must be at least ${fieldRules.min}`;
        return;
      }
    }

    // Max value validation
    if (value !== null && value !== undefined && fieldRules.max !== undefined) {
      const num = Number(value);
      if (!isNaN(num) && num > fieldRules.max) {
        errors[field] = fieldRules.message ||
          `${field} must be at most ${fieldRules.max}`;
        return;
      }
    }

    // Custom validation
    if (value && fieldRules.custom) {
      const customError = fieldRules.custom(value, data);
      if (customError) {
        errors[field] = fieldRules.message || customError;
        return;
      }
    }
  });

  return errors;
}

/**
 * Check if form is valid
 */
export function isFormValid<T extends Record<string, unknown>>(
  errors: Record<string, string>
): boolean {
  return Object.keys(errors).length === 0;
}

/**
 * Validate password confirmation
 */
export function validatePasswordConfirmation(
  password: string,
  confirmation: string
): string | null {
  if (!confirmation) return 'Please confirm your password';
  if (password !== confirmation) return 'Passwords do not match';
  return null;
}
