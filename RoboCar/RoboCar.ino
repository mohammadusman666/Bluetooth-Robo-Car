char t; // character sent by app
int const m1En = 11; // motor 1 enable
int const m1Fwd = 9; // motor 1 forward
int const m1Bwd = 8; // motor 1 backward
int const m2En = 10; // motor 2 enable
int const m2Fwd = 7; // motor 2 forward
int const m2Bwd = 6; // motor 2 backward

void setup()
{
  pinMode(m1En, OUTPUT);
  digitalWrite(m1En, HIGH); // enable motor 1
  pinMode(m2En, OUTPUT);
  digitalWrite(m2En, HIGH); // enable motor 2
  pinMode(m1Fwd, OUTPUT);  // left motor forward
  pinMode(m2Fwd, OUTPUT);  // left motor reverse
  pinMode(m1Bwd, OUTPUT);  // right motor forward
  pinMode(m2Bwd, OUTPUT);  // right motor reverse
  Serial.begin(9600);
}

void loop()
{
  if (Serial.available())
  {
    t = Serial.read(); // read from serial
    Serial.println(t);
  }

  if (t == 'F') // move forward (both motors rotate in forward direction)
  {
    digitalWrite(m1Fwd, HIGH);
    digitalWrite(m2Fwd, HIGH);
  }
  else if (t == 'B') // move backward (both motors rotate in backward direction)
  {
    digitalWrite(m1Bwd, HIGH);
    digitalWrite(m2Bwd, HIGH);
  }
  else if (t == 'L') // turn right (left side motors rotate in forward direction, right side motor doesn't rotate)
  {
    digitalWrite(m1Fwd, HIGH);
  }
  else if (t == 'R') // turn left (right side motors rotate in forward direction, left side motor doesn't rotate)
  {
    digitalWrite(m2Fwd, HIGH);
  }
  else if (t == 'S') // stop (all motors stop)
  {
    digitalWrite(m1Fwd, LOW);
    digitalWrite(m2Fwd, LOW);
    digitalWrite(m1Bwd, LOW);
    digitalWrite(m2Bwd, LOW);
  }
}
