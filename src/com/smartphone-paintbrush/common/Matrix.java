package com.internalerror.common;

public final class Matrix {
	
	/**
	 * Converts an array to a matrix.
	 * @param arr array to convert.
	 * @param mat matrix to fill.
	 */
	public static void arrayToMatrix(float[] arr, float[][] mat) {
		if (arr.length != mat.length * mat[0].length) {
			throw new IllegalArgumentException("Array length not multiple of rows.");
		}
		for (int i = 0; i < mat.length; ++i) {
			for (int j = 0; j < mat[0].length; ++j) {
				mat[i][j] = arr[i*mat[0].length + j];
			}
		}
	}
	
	/**
	 * Constructs a new matrix.
	 * @param rows number of rows the new matrix should have.
	 * @param cols number of columns the new matrix should have.
	 * @return new matrix.
	 */
	public static float[][] makeNew(int rows, int cols) {
		return new float[rows][cols];
	}

	public static float[][] makeNewAndFill(int rows, int cols, float entry) {
		float[][] mat = new float[rows][cols];
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				mat[i][j] = entry;
			}
		}
		return mat;
	}
	
	public static float[][] makeNewIdentity(int dim) {
		float[][] mat = new float[dim][dim];
		for (int i = 0; i < dim; ++i) {
			mat[i][i] = 1;
		}
		return mat;
	}
	
	public static void identity(float[][] mat) {
		if (mat.length != mat[0].length) {
			throw new IllegalArgumentException("Matrix not square.");
		}
		for (int i = 0; i < mat.length; ++i) {
			for (int j = 0; j < mat[0].length; ++j) {
				if (i == j) {
					mat[i][j] = 1;
				} else {
					mat[i][j] = 0;
				}
			}
		}
	}
	
	public static void fill(float[][] mat, float entry) {
		for (int i = 0; i < mat.length; ++i) {
			for (int j = 0; j < mat[0].length; ++j) {
				mat[i][j] = entry;
			}
		}
	}
	
	public static String matrixToString(float[][] mat) {
		String str = new String();
		for (int i = 0; i < mat.length; ++i) {
			for (int j = 0; j < mat[0].length; ++j) {
				str += String.valueOf(mat[i][j]) + "\t";
			}
			str += "\n";
		}
		return str;
	}
	
	// mat = mat^t
	public static void transpose(float[][] mat) {
		if (mat.length != mat[0].length) {
			throw new IllegalArgumentException("Matrix not square.");
		}
		float temp;
		for (int i = 1; i < mat.length; ++i) {
			for (int j = 0; j < i; ++j) {
				temp = mat[i][j];
				mat[i][j] = mat[j][i];
				mat[j][i] = temp;
			}
		}
	}
	
	// resultMat = mat^t
	public static void transpose(float[][] mat, float[][] resultMat) {
		if (mat == resultMat) {
			transpose(mat);
			return;
		}
		if (mat.length != resultMat[0].length || mat[0].length != resultMat.length) {
			throw new IllegalArgumentException("Dimension mismatch.");
		}
		for (int i = 0; i < mat.length; ++i) {
			for (int j = 0; j < mat[0].length; ++j) {
				resultMat[j][i] = mat[i][j];
			}
		}
	}
	
	// resultMat = mat1 + mat2
	public static void add(float[][] mat1, float[][] mat2, float[][] resultMat) {
		if (mat1.length != mat2.length || mat1[0].length != mat2[0].length ||
			mat1.length != resultMat.length || mat1[0].length != resultMat[0].length) {
			throw new IllegalArgumentException("Dimension mismatch.");
		}
		for (int i = 0; i < mat1.length; ++i) {
			for (int j = 0; j < mat1[0].length; ++j) {
				resultMat[i][j] = mat1[i][j] + mat2[i][j];
			}
		}
	}
	
	// vec1 += vec2
	public static void addTo(float[] vec1, float[] vec2) {
		if (vec1.length != vec2.length) {
			throw new IllegalArgumentException("Dimension mismatch.");
		}
		for (int i = 0; i < vec1.length; ++i) {
			vec1[i] += vec2[i];
		}
	}
	
	// mat1 += mat2
	public static void addTo(float[][] mat1, float[][] mat2) {
		if (mat1.length != mat2.length || mat1[0].length != mat2[0].length) {
			throw new IllegalArgumentException("Dimension mismatch.");
		}
		for (int i = 0; i < mat1.length; ++i) {
			for (int j = 0; j < mat1[0].length; ++j) {
				mat1[i][j] += mat2[i][j];
			}
		}
	}
	
	// resultMat = mat1 - mat2
	public static void sub(float[][] mat1, float[][] mat2, float[][] resultMat) {
		if (mat1.length != mat2.length || mat1[0].length != mat2[0].length ||
			mat1.length != resultMat.length || mat1[0].length != resultMat[0].length) {
			throw new IllegalArgumentException("Dimension mismatch.");
		}
		for (int i = 0; i < mat1.length; ++i) {
			for (int j = 0; j < mat1[0].length; ++j) {
				resultMat[i][j] = mat1[i][j] - mat2[i][j];
			}
		}
	}
	
	// vec1 -= vec2
	public static void subFrom(float[] vec1, float[] vec2) {
		if (vec1.length != vec2.length) {
			throw new IllegalArgumentException("Dimension mismatch.");
		}
		for (int i = 0; i < vec1.length; ++i) {
			vec1[i] -= vec2[i];
		}
	}

	// mat1 -= mat2
	public static void subFrom(float[][] mat1, float[][] mat2) {
		if (mat1.length != mat2.length || mat1[0].length != mat2[0].length) {
			throw new IllegalArgumentException("Dimension mismatch.");
		}
		for (int i = 0; i < mat1.length; ++i) {
			for (int j = 0; j < mat1[0].length; ++j) {
				mat1[i][j] -= mat2[i][j];
			}
		}
	}
	
	// resultMat = mat * scalar
	public static void mult(float[][] mat, float scalar, float[][] resultMat) {
		if (mat.length != resultMat.length || mat[0].length != resultMat[0].length) {
			throw new IllegalArgumentException("Dimension mismatch");
		}
		for (int i = 0; i < mat.length; ++i) {
			for (int j = 0; j < mat[0].length; ++j) {
				resultMat[i][j] = mat[i][j] * scalar;
			}
		}
	}
	
	
	// resultVec = mat * vec
	public static void mult(float[][] mat, float[] vec, float[] resultVec) {
		if (vec == resultVec) {
			throw new IllegalArgumentException("resultVec vector cannot be vec.");
		} else if (mat[0].length != vec.length || mat.length != resultVec.length) {
			throw new IllegalArgumentException("Dimension mismatch.");
		}
		float sum;
		for (int i = 0; i < mat.length; ++i) {
			sum = 0f;
			for (int j = 0; j < vec.length; ++j) {
				sum += mat[i][j] * vec[j];
			}
			resultVec[i] = sum;
		}
	}

	// resultMat = mat1 * mat2
	public static void mult(float[][] mat1, float[][] mat2, float[][] resultMat) {
		if (resultMat == mat1 || resultMat == mat2) {
			throw new IllegalArgumentException("resultMat matrix cannot be mat1 or mat2.");
		} else if (mat1[0].length != mat2.length ||
				   mat1.length != resultMat.length || mat2[0].length != resultMat[0].length) {
			throw new IllegalArgumentException("Dimension mismatch.");
		}
		float sum;
		for (int i = 0; i < mat1.length; ++i) {
			for (int j = 0; j < mat2[0].length; ++j) {
				sum = 0f;
				for (int k = 0; k < mat2.length; ++k) {
					sum += mat1[i][k] * mat2[k][j];
				}
				resultMat[i][j] = sum;
			}
		}
	}
	
	// vec *= scalar
	public static void multBy(float[] vec, float scalar) {
		for (int i = 0; i < vec.length; ++i) {
			vec[i] *= scalar;
		}
	}
	
	// mat *= scalar
	public static void multBy(float[][] mat, float scalar) {
		for (int i = 0; i < mat.length; ++i) {
			for (int j = 0; j < mat[0].length; ++j) {
				mat[i][j] *= scalar;
			}
		}
	}
	
	public static String vecToString(float[] vec) {
		StringBuffer toRet = new StringBuffer();
		toRet.append("[");
		for (int i = 0; i < vec.length - 1; i++) {
			toRet.append(vec[i] + "\t");
		}
		toRet.append(vec[vec.length-1] + "]");
		return toRet.toString();
	}
	
	public static String vecToCSV(float[] vec) {
		StringBuffer toRet = new StringBuffer();
		for (int i = 0; i < vec.length; i++) {
			toRet.append("," + vec[i]);
		}
		return toRet.toString();
	}
}