package engine;

public abstract class CGTValue {

	public abstract String toString();

	public abstract boolean equals(Object object);

	public static CGTValue combine(CGTValue leftValue, CGTValue rightValue) {
		if (leftValue == null && rightValue == null) {
			// Berlekamp et al., p. 39
			return new Number(0);
		}

		if (leftValue == null && rightValue != null) {
			if (rightValue instanceof Number) {
				// Berlekamp et al., p. 44
				Number right = (Number) rightValue;
				return new Number(right.getValue() - 1);
			} else if (rightValue instanceof Nimber) {
				return new Number(0);
			}
		}

		if (leftValue != null && rightValue == null) {
			if (leftValue instanceof Number) {
				// Berlekamp et al., p. 44
				Number left = (Number) leftValue;
				return new Number(left.getValue() + 1);
			} else if (leftValue instanceof Nimber) {
				return new Number(0);
			}
		}

		if (leftValue instanceof Number) {
			Number left = (Number) leftValue;

			if (rightValue instanceof Number) {
				Number right = (Number) rightValue;
				// For fractions, see Berlekamp et al., p. 41
				// For switches, see Berlekamp et al., p. 141

				if (left.getValue() == 0 && right.getValue() == 0) {
					return new Nimber(1);
				}

                if (left.getValue() == right.getValue()) {
					return new Number(left.getValue());
				}

				if (left.getValue() > right.getValue()) {
					return new Switch(left, right);
				}

				if (left.getValue() + 1 == right.getValue()) {
					// {n|n+1}=n+1/2 (Berlekamp et al., p. 27)
					return new Number(left.getValue() + 0.5);
				}

				if (left.getValue() < 0 && right.getValue() > 0) {
					return new Number(0);
				}

				if (left.getValue() >= 0 && right.getValue() > 0) {
					return new Number(left.getValue() + 1);
				}

				if (left.getValue() < 0 && right.getValue() <= 0) {
					return new Number(right.getValue() - 1);
				}
            }
			else if (rightValue instanceof Nimber) {
				if (left.getValue() > 0) {
					return new Number(left.getValue());
				} else if (left.getValue() == 0) {
					return new Infinitesimal(1);
				} else {
					return new Number(0);
				}
			} else if (rightValue instanceof Switch) {
				Switch right = (Switch) rightValue;
                if (right.isPositive()) {
                    if (left.getValue() > 0) {
						return new Number(left.getValue());
					} else if (left.getValue() == 0) {
						return new Infinitesimal(1);
					} else {
						return new Number(0);
					}
                } else if (right.isNegative()) {
                    if (left.getValue() > 0) {
						return new Nimber(1);
					} else if (left.getValue() == 0) {
						return new Nimber(1);
					} else {
						return new Number(left.getValue());
					}
				} else {
					if (left.getValue() > 0) {
						return new Number(left.getValue());
					} else if (left.getValue() == 0) {
						return new Infinitesimal(1);
					} else {
						return new Number(0);
					}
				}
			} else if (rightValue instanceof Infinitesimal) {
				Infinitesimal right = (Infinitesimal) rightValue;
				if (right.getValue() > 0) {
					if (left.getValue() > 0) {
						return new Number(left.getValue());
					} else if (left.getValue() == 0) {
						return new Infinitesimal(1);
					} else {
						return new Number(0);
					}
				} else {
					if (left.getValue() > 0) {
						return new Nimber(1);
					} else if (left.getValue() == 0) {
						return new Nimber(1);
					} else {
						return new Number(left.getValue());
					}
				}
			}

		} else if (leftValue instanceof Nimber) {
			Nimber left = (Nimber) leftValue;

			if (rightValue instanceof Number) {
				Number right = (Number) rightValue;
				if (right.getValue() > 0) {
					return new Number(0);
				} else if (right.getValue() == 0) {
					return new Infinitesimal(-1);
				} else {
					return new Number(right.getValue());
				}
			} else if (rightValue instanceof Nimber) {
				Nimber right = (Nimber) rightValue;
				int result = left.getValue() ^ right.getValue();
				if (result == 0) {
					return new Number(0);
				} else {
					return new Nimber(result);
				}
			}

		} else if (leftValue instanceof Switch) {
			Switch left = (Switch) leftValue;

			if (rightValue instanceof Number) {
				Number right = (Number) rightValue;
                if (left.isPositive()) {
                    if (right.getValue() > 0) {
						return new Number(right.getValue());
					} else if (right.getValue() == 0) {
						return new Nimber(1);
					} else {
						return new Nimber(1);
					}
                } else if (left.isNegative()) {
                    if (right.getValue() > 0) {
						return new Number(0);
					} else if (right.getValue() == 0) {
						return new Infinitesimal(-1);
					} else {
						return new Number(right.getValue());
					}
				} else {
					if (right.getValue() > 0) {
						return new Number(0);
					} else if (right.getValue() == 0) {
						return new Infinitesimal(-1);
					} else {
						return new Number(right.getValue());
					}
				}
			}

		} else if (leftValue instanceof Infinitesimal) {
			Infinitesimal left = (Infinitesimal) leftValue;

			if (rightValue instanceof Number) {
				Number right = (Number) rightValue;
				if (left.getValue() > 0) {
					if (right.getValue() > 0) {
						return new Number(right.getValue());
					} else if (right.getValue() == 0) {
						return new Nimber(1);
					} else {
						return new Nimber(1);
					}
				} else {
					if (right.getValue() > 0) {
						return new Number(0);
					} else if (right.getValue() == 0) {
						return new Infinitesimal(-1);
					} else {
						return new Number(right.getValue());
					}
				}
			}
		}
		// If value cannot be simplified, return nothing and carry on with
		// Alpha-Beta-Search
		return null;
	}

	public static CGTValue max(CGTValue firstValue, CGTValue secondValue, boolean blackTurn) {

		if (firstValue == null) {
			return secondValue;
		}

		if (secondValue == null) {
			return firstValue;
		}

		if (firstValue instanceof Number) {
			Number first = (Number) firstValue;

			if (secondValue instanceof Number) {
				Number second = (Number) secondValue;

				if (blackTurn) {
					if (first.getValue() >= second.getValue()) {
						return first;
					} else {
						return second;
					}
				} else {
					if (first.getValue() <= second.getValue()) {
						return first;
					} else {
						return second;
					}
				}

			} else if (secondValue instanceof Nimber) {
				Nimber second = (Nimber) secondValue;

				if (blackTurn) {
					if (first.getValue() >= 0) {
						return first;
					} else {
						return second;
					}
				} else {
					if (first.getValue() <= 0) {
						return first;
					} else {
						return second;
					}
				}

			} else if (secondValue instanceof Switch) {
				Switch second = (Switch) secondValue;

				if (blackTurn) {
					if (first.getValue() >= 0) {
						return first;
                    } else {
                        return second;
					}
				} else {
					if (first.getValue() <= 0) {
						return first;
					} else {
                        return second;
                    }
				}
			} else if (secondValue instanceof Infinitesimal) {
				Infinitesimal second = (Infinitesimal) secondValue;

				if (blackTurn) {
					if (first.getValue() >= 0) {
						return first;
					} else {
						return second;
					}
				} else {
					if (first.getValue() <= 0) {
						return first;
					} else {
						return second;
					}
				}
			}
		} else if (firstValue instanceof Nimber) {
			Nimber first = (Nimber) firstValue;

			if (secondValue instanceof Number) {
				Number second = (Number) secondValue;

				if (blackTurn) {
					if (second.getValue() >= 0) {
						return second;
					} else {
						return first;
					}
				} else {
					if (second.getValue() <= 0) {
						return second;
					} else {
						return first;
					}
				}
			} else if (secondValue instanceof Nimber) {
				Nimber second = (Nimber) secondValue;

				// Smaller nimber means smaller loss
				if (first.getValue() < second.getValue()) {
					return first;
				} else {
					return second;
				}
			} else if (secondValue instanceof Switch) {
				Switch second = (Switch) secondValue;

				if (blackTurn) {
					if(second.isPositive()) {
						return second;
					} else {
						return first;
					}
				} else {
					if(second.isNegative()) {
						return second;
					} else {
						return first;
					}
				}
			} else if (secondValue instanceof Infinitesimal) {
				Infinitesimal second = (Infinitesimal) secondValue;

				if (blackTurn) {
					if(second.getValue() > 0) {
						return second;
					} else {
						return first;
					}
				} else {
					if(second.getValue() < 0) {
						return second;
					} else {
						return first;
					}
				}
			}
		} else if (firstValue instanceof Switch) {
			Switch first = (Switch)firstValue;

			if (secondValue instanceof Number) {
				Number second = (Number)secondValue;

				if(blackTurn) {
					if (second.getValue() >= 0) {
						return second;
                    } else {
                        return first;
					}
				} else {
					if (second.getValue() <= 0) {
						return second;
					} else {
                        return first;
                    }
				}
			} else if (secondValue instanceof Nimber) {
				Nimber second = (Nimber)secondValue;

				if(blackTurn) {
					if(first.isPositive()) {
						return first;
					} else {
						return second;
					}
				} else {
					if(first.isNegative()) {
						return first;
					} else {
						return second;
					}
				}
			} else if (secondValue instanceof Switch) {
				Switch second = (Switch)secondValue;

				if(blackTurn) {
					if(first.isPositive()) {
						return first;
					} else {
						return second;
					}
				} else {
					if(first.isNegative()) {
						return first;
					} else {
						return second;
					}
				}
			} else if (secondValue instanceof Infinitesimal) {
				Infinitesimal second = (Infinitesimal)secondValue;

				if(blackTurn) {
					if(first.isPositive()) {
						return first;
					} else {
						return second;
					}
				} else {
					if(first.isNegative()) {
						return first;
					} else {
						return second;
					}
				}
			}
		} else if (firstValue instanceof Infinitesimal) {
			Infinitesimal first = (Infinitesimal)firstValue;

			if (secondValue instanceof Number) {
				Number second = (Number)secondValue;

				if(blackTurn) {
					if(second.getValue() >= 0) {
						return second;
					} else {
						return first;
					}
				} else  {
					if(second.getValue() <= 0) {
						return second;
					} else {
						return first;
					}
				}
			} else if (secondValue instanceof Nimber) {
				Nimber second = (Nimber)secondValue;

				if(blackTurn) {
					if(first.getValue() > 0) {
						return first;
					} else {
						return second;
					}
				} else {
					if(first.getValue() < 0) {
						return first;
					} else {
						return second;
					}
				}
			} else if (secondValue instanceof Switch) {
				Switch second = (Switch)secondValue;

				if(blackTurn) {
					if(second.isPositive()) {
						return second;
					} else {
						return first;
					}
				} else {
					if(second.isNegative()) {
						return second;
					} else {
						return first;
					}
				}
			} else if (secondValue instanceof Infinitesimal) {
				Infinitesimal second = (Infinitesimal)secondValue;

				if(blackTurn) {
					if(first.getValue() >= second.getValue()) {
						return first;
					} else {
						return second;
					}
				} else {
					if(first.getValue() <= second.getValue()) {
						return first;
					} else {
						return second;
					}
				}
			}
		}

		throw new IllegalArgumentException("Cannot compare given values.");
	}
}
