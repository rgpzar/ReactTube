import { getAuthSession } from './login';

import {describe, expect, it} from '@jest/globals';

describe('getAuthSession', () => {
  it('should return a JWT token when valid credentials are provided', async () => {
    // Arrange
    const userDetails = {
      username: 'testuser',
      password: 'testpassword',
    };

    // Act
    const jwt = await getAuthSession(userDetails);

    // Assert
    expect(jwt).toBeDefined();
    expect(typeof jwt).toBe('string');
  });

  it('should throw an error when invalid credentials are provided', async () => {
    // Arrange
    const userDetails = {
      username: 'invaliduser',
      password: 'invalidpassword',
    };

    // Act and Assert
    await expect(getAuthSession(userDetails)).rejects.toThrow('Invalid username or password');
  });
});