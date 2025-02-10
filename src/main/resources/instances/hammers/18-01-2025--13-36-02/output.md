# Description

The AssemblyChain model is designed to represent a manufacturing assembly line, specifically focusing on the assembly of hammers from their component parts. The model is structured using classes and attributes, which are then organized into relationships that define the interactions between these classes.

### Components

1. **Element (Abstract Class)**
   - Attributes: 
     - `x: Integer` - Represents the x-coordinate of the element.
     - `y: Integer` - Represents the y-coordinate of the element.
   - Purpose: Base class for elements in the assembly line, providing spatial attributes.

2. **Machine (Abstract Class)**
   - Inherits from: Element
   - Attributes: 
     - `processingTime: Integer` - The time taken by the machine to process a piece.
   - Operations: 
     - `start()` - Starts the machine.
     - `stop()` - Stops the machine.
   - Purpose: Represents any machine on the assembly line.

3. **PieceGenerator (Abstract Class)**
   - Inherits from: Machine
   - Purpose: Serves as a base class for machines that generate parts.

4. **HandleGenerator (Class)**
   - Inherits from: PieceGenerator
   - Attributes:
     - `counter: Integer` - Tracks the number of handles generated.

5. **HeadGenerator (Class)**
   - Inherits from: PieceGenerator
   - Attributes:
     - `counter: Integer` - Tracks the number of heads generated.

6. **Assembler (Class)**
   - Inherits from: Machine
   - Purpose: Assembles heads and handles into hammers.

7. **Polisher (Class)**
   - Inherits from: Machine
   - Purpose: Polishes hammers after assembly.

8. **Tray (Class)**
   - Inherits from: Element
   - Attributes:
     - `capacity: Integer` - Maximum number of pieces that the tray can hold.

9. **Piece (Abstract Class)**
   - Purpose: Base class for all types of parts and assembled products.

10. **Head (Class)**
    - Inherits from: Piece

11. **Handle (Class)**
    - Inherits from: Piece

12. **Hammer (Class)**
    - Inherits from: Piece
    - Attributes:
      - `isPolished: Boolean` - Indicates if the hammer is polished.

# Relationships

1. **Output Association**
   - Between: Machine [1..*] (input) and Tray [1] (output)
   - Purpose: Denotes that each machine outputs to one tray, and a tray can have inputs from multiple machines.

2. **Input Association**
   - Between: Tray [*] (input) and Machine [*] (output)
   - Purpose: Indicates trays can feed into multiple machines, and machines can receive input from multiple trays.

3. **Content Aggregation**
   - Between: Tray [1] and Piece [*] (ordered)
   - Purpose: Shows that each tray can hold multiple pieces, and pieces within a tray are ordered.

# Invariants

1. **Tray AtLeastOneCell**
   - `self.capacity > 0`: Ensures that a tray must have a capacity greater than zero.

2. **Tray PiecesSameType**
   - Ensures all pieces in a tray are of the same type, either all Hammers, all Handles, or all Heads.

3. **Assembler OneinputOfHandles**
   - Requires that the assembler must have at least one tray of inputs that contains only Handles.

4. **Assembler OneinputOfHeads**
   - Requires that the assembler must have at least one tray of inputs that contains only Heads.

5. **Polisher HammersInput**
   - Requires that all inputs to the polisher are trays containing only Hammers.

6. **HeadGenerator HeadsOutput**
   - All pieces output by a HeadGenerator must be Heads.

7. **HandleGenerator HandlesOutput**
   - All pieces output by a HandleGenerator must be Handles.

8. **Assembler HammersOutput**
   - All pieces output by an Assembler must be Hammers.

9. **PieceGenerator NoInput**
   - Ensures that piece generators have no input trays, as they generate parts from raw materials.

# Category: Realistic but invalid
Create instances that make sense in real-life scenarios but violate constraints exposing overly restrictive or unrealistic constraints.

### Instance 1: Overloaded Tray

**Description:** A scenario where a tray is overloaded, violating the `Tray AtLeastOneCell` invariant.

- **Machine:**
  - **HandleGenerator1:**
    - `x: 10`
    - `y: 20`
    - `processingTime: 5`
    - `counter: 50`

- **Tray:**
  - **Tray1:**
    - `x: 15`
    - `y: 25`
    - `capacity: 0`  // Invalid: Tray capacity must be greater than 0
    - Contains: 
      - 50 Instances of **Handle**

- **Relationships:**
  - **Output Association:**
    - Machine: HandleGenerator1 -> Tray: Tray1

**Explanation:** The tray `Tray1` has a capacity of `0`, which violates the constraint that a tray must have a capacity greater than zero.

---

### Instance 2: Mixed Content Tray

**Description:** A scenario where a tray contains mixed types of parts, violating the `Tray PiecesSameType` invariant.

- **Machine:**
  - **HandleGenerator2:**
    - `x: 5`
    - `y: 15`
    - `processingTime: 4`
    - `counter: 10`

  - **HeadGenerator1:**
    - `x: 6`
    - `y: 16`
    - `processingTime: 6`
    - `counter: 5`

- **Tray:**
  - **Tray2:**
    - `x: 10`
    - `y: 20`
    - `capacity: 20`
    - Contains:
      - 10 Instances of **Handle**
      - 5 Instances of **Head**  // Invalid: Mixed content in tray

- **Relationships:**
  - **Output Association:**
    - Machines: HandleGenerator2, HeadGenerator1 -> Tray: Tray2

**Explanation:** The tray `Tray2` contains both Handles and Heads, which violates the constraint that all pieces in a tray must be of the same type.

---

### Instance 3: Assembler Without Handles

**Description:** A scenario where an assembler is missing an input tray of handles, violating the `Assembler OneinputOfHandles` invariant.

- **Machine:**
  - **Assembler1:**
    - `x: 20`
    - `y: 30`
    - `processingTime: 8`

- **Tray:**
  - **Tray3:**
    - `x: 25`
    - `y: 35`
    - `capacity: 15`
    - Contains:
      - 10 Instances of **Head**

- **Relationships:**
  - **Input Association:**
    - Tray: Tray3 -> Machine: Assembler1

**Explanation:** The assembler `Assembler1` only has an input tray of heads and is missing a required input tray of handles, violating the invariant that an assembler must have at least one tray of handles.

---

### Instance 4: Polisher Without Hammers

**Description:** A scenario where a polisher is fed trays that do not contain hammers, violating the `Polisher HammersInput` invariant.

- **Machine:**
  - **Polisher1:**
    - `x: 30`
    - `y: 40`
    - `processingTime: 10`

- **Tray:**
  - **Tray4:**
    - `x: 35`
    - `y: 45`
    - `capacity: 20`
    - Contains:
      - 15 Instances of **Handle**  // Invalid: Polisher requires hammers

- **Relationships:**
  - **Input Association:**
    - Tray: Tray4 -> Machine: Polisher1

**Explanation:** The tray `Tray4` feeding into `Polisher1` contains handles rather than hammers, violating the constraint that polishers must only receive trays containing hammers.

---

### Instance 5: PieceGenerator With Input

**Description:** A scenario where a piece generator is incorrectly associated with an input tray, violating the `PieceGenerator NoInput` invariant.

- **Machine:**
  - **HandleGenerator3:**
    - `x: 40`
    - `y: 50`
    - `processingTime: 7`
    - `counter: 30`

- **Tray:**
  - **Tray5:**
    - `x: 45`
    - `y: 55`
    - `capacity: 25`
    - Contains:
      - 20 Instances of **Handle**

- **Relationships:**
  - **Input Association:**
    - Tray: Tray5 -> Machine: HandleGenerator3  // Invalid: Piece generators should not have inputs

**Explanation:** The `HandleGenerator3` is incorrectly set to receive input from `Tray5`, violating the constraint that piece generators should not have any input trays.
```
!new HandleGenerator('HandleGenerator1')
!HandleGenerator1.x:=10
!HandleGenerator1.y:=20
!HandleGenerator1.processingTime:=5
!HandleGenerator1.counter:=50

!new Tray('Tray1')
!Tray1.x:=15
!Tray1.y:=25
!Tray1.capacity:=0

!insert(HandleGenerator1,Tray1) into Output

!new HandleGenerator('HandleGenerator2')
!HandleGenerator2.x:=5
!HandleGenerator2.y:=15
!HandleGenerator2.processingTime:=4
!HandleGenerator2.counter:=10

!new HeadGenerator('HeadGenerator1')
!HeadGenerator1.x:=6
!HeadGenerator1.y:=16
!HeadGenerator1.processingTime:=6
!HeadGenerator1.counter:=5

!new Tray('Tray2')
!Tray2.x:=10
!Tray2.y:=20
!Tray2.capacity:=20

!insert(HandleGenerator2,Tray2) into Output
!insert(HeadGenerator1,Tray2) into Output

!new Assembler('Assembler1')
!Assembler1.x:=20
!Assembler1.y:=30
!Assembler1.processingTime:=8

!new Tray('Tray3')
!Tray3.x:=25
!Tray3.y:=35
!Tray3.capacity:=15

!insert(Tray3,Assembler1) into Input

!new Polisher('Polisher1')
!Polisher1.x:=30
!Polisher1.y:=40
!Polisher1.processingTime:=10

!new Tray('Tray4')
!Tray4.x:=35
!Tray4.y:=45
!Tray4.capacity:=20

!insert(Tray4,Polisher1) into Input

!new HandleGenerator('HandleGenerator3')
!HandleGenerator3.x:=40
!HandleGenerator3.y:=50
!HandleGenerator3.processingTime:=7
!HandleGenerator3.counter:=30

!new Tray('Tray5')
!Tray5.x:=45
!Tray5.y:=55
!Tray5.capacity:=25

!insert(Tray5,HandleGenerator3) into Input
```

# Category: Complex Instances
Describe complex instances involving multiple interrelated entities, testing interactions between constraints, relationships, and multiplicities.

## Instance 1: Efficient Assembly Line in Germany

### Elements and Machines

1. **HandleGenerator_HG1**
   - Location: `(x: 5, y: 10)`
   - `processingTime: 2` (seconds)
   - `counter: 100`
   - Outputs to: `Tray_T1`

2. **HeadGenerator_HG2**
   - Location: `(x: 10, y: 10)`
   - `processingTime: 3` (seconds)
   - `counter: 100`
   - Outputs to: `Tray_T2`

3. **Assembler_A1**
   - Location: `(x: 15, y: 10)`
   - `processingTime: 5` (seconds)
   - Inputs from: `Tray_T1` (Handles), `Tray_T2` (Heads)
   - Outputs to: `Tray_T3`

4. **Polisher_P1**
   - Location: `(x: 20, y: 10)`
   - `processingTime: 4` (seconds)
   - Inputs from: `Tray_T3`
   - Outputs to: `Tray_T4`

### Trays

1. **Tray_T1**
   - `capacity: 50`
   - Contains: 40 `Handle` instances

2. **Tray_T2**
   - `capacity: 50`
   - Contains: 40 `Head` instances

3. **Tray_T3**
   - `capacity: 50`
   - Contains: 30 `Hammer` instances (all `isPolished: False`)

4. **Tray_T4**
   - `capacity: 50`
   - Contains: 30 `Hammer` instances (all `isPolished: True`)

### Pieces

- 40 `Handle` instances in `Tray_T1`
- 40 `Head` instances in `Tray_T2`
- 30 `Hammer` instances in `Tray_T3` and `Tray_T4`

### Context and Constraints

- **Region:** Germany
- **Invariants:** All invariants are satisfied: `Tray AtLeastOneCell`, `Tray PiecesSameType`, `Assembler OneinputOfHandles`, `Assembler OneinputOfHeads`, `Polisher HammersInput`, `HeadGenerator HeadsOutput`, `HandleGenerator HandlesOutput`, `Assembler HammersOutput`, `PieceGenerator NoInput`.

## Instance 2: Advanced Assembly Line in Japan

### Elements and Machines

1. **HandleGenerator_HG3**
   - Location: `(x: 8, y: 20)`
   - `processingTime: 2` (seconds)
   - `counter: 150`
   - Outputs to: `Tray_T5`

2. **HeadGenerator_HG4**
   - Location: `(x: 12, y: 20)`
   - `processingTime: 3` (seconds)
   - `counter: 150`
   - Outputs to: `Tray_T6`

3. **Assembler_A2**
   - Location: `(x: 18, y: 20)`
   - `processingTime: 6` (seconds)
   - Inputs from: `Tray_T5` (Handles), `Tray_T6` (Heads)
   - Outputs to: `Tray_T7`

4. **Polisher_P2**
   - Location: `(x: 25, y: 20)`
   - `processingTime: 5` (seconds)
   - Inputs from: `Tray_T7`
   - Outputs to: `Tray_T8`

### Trays

1. **Tray_T5**
   - `capacity: 60`
   - Contains: 50 `Handle` instances

2. **Tray_T6**
   - `capacity: 60`
   - Contains: 50 `Head` instances

3. **Tray_T7**
   - `capacity: 60`
   - Contains: 45 `Hammer` instances (all `isPolished: False`)

4. **Tray_T8**
   - `capacity: 60`
   - Contains: 45 `Hammer` instances (all `isPolished: True`)

### Pieces

- 50 `Handle` instances in `Tray_T5`
- 50 `Head` instances in `Tray_T6`
- 45 `Hammer` instances in `Tray_T7` and `Tray_T8`

### Context and Constraints

- **Region:** Japan
- **Invariants:** All invariants are satisfied: `Tray AtLeastOneCell`, `Tray PiecesSameType`, `Assembler OneinputOfHandles`, `Assembler OneinputOfHeads`, `Polisher HammersInput`, `HeadGenerator HeadsOutput`, `HandleGenerator HandlesOutput`, `Assembler HammersOutput`, `PieceGenerator NoInput`.

These instances showcase a structured and regional approach to hammer assembly lines, incorporating variations in processing times and capacities to adapt to different manufacturing environments in Germany and Japan.
```
!new HandleGenerator('HandleGenerator_HG1')
!HandleGenerator_HG1.Location:=(x: 5, y: 10)
!HandleGenerator_HG1.processingTime:=2
!HandleGenerator_HG1.counter:=100
!new Tray('Tray_T1')
!Tray_T1.capacity:=50
!insert(HandleGenerator_HG1, Tray_T1) into Output

!new HeadGenerator('HeadGenerator_HG2')
!HeadGenerator_HG2.Location:=(x: 10, y: 10)
!HeadGenerator_HG2.processingTime:=3
!HeadGenerator_HG2.counter:=100
!new Tray('Tray_T2')
!Tray_T2.capacity:=50
!insert(HeadGenerator_HG2, Tray_T2) into Output

!new Assembler('Assembler_A1')
!Assembler_A1.Location:=(x: 15, y: 10)
!Assembler_A1.processingTime:=5
!new Tray('Tray_T3')
!Tray_T3.capacity:=50
!insert(Tray_T1, Assembler_A1) into Input
!insert(Tray_T2, Assembler_A1) into Input
!insert(Assembler_A1, Tray_T3) into Output

!new Polisher('Polisher_P1')
!Polisher_P1.Location:=(x: 20, y: 10)
!Polisher_P1.processingTime:=4
!new Tray('Tray_T4')
!Tray_T4.capacity:=50
!insert(Tray_T3, Polisher_P1) into Input
!insert(Polisher_P1, Tray_T4) into Output

!repeat 40 times
!new Handle('Handle_H', Tray_T1) into Content

!repeat 40 times
!new Head('Head_H', Tray_T2) into Content

!repeat 30 times
!new Hammer('Hammer_H', Tray_T3) isPolished:=False into Content

!repeat 30 times
!new Hammer('Hammer_H', Tray_T4) isPolished:=True into Content


!new HandleGenerator('HandleGenerator_HG3')
!HandleGenerator_HG3.Location:=(x: 8, y: 20)
!HandleGenerator_HG3.processingTime:=2
!HandleGenerator_HG3.counter:=150
!new Tray('Tray_T5')
!Tray_T5.capacity:=60
!insert(HandleGenerator_HG3, Tray_T5) into Output

!new HeadGenerator('HeadGenerator_HG4')
!HeadGenerator_HG4.Location:=(x: 12, y: 20)
!HeadGenerator_HG4.processingTime:=3
!HeadGenerator_HG4.counter:=150
!new Tray('Tray_T6')
!Tray_T6.capacity:=60
!insert(HeadGenerator_HG4, Tray_T6) into Output

!new Assembler('Assembler_A2')
!Assembler_A2.Location:=(x: 18, y: 20)
!Assembler_A2.processingTime:=6
!new Tray('Tray_T7')
!Tray_T7.capacity:=60
!insert(Tray_T5, Assembler_A2) into Input
!insert(Tray_T6, Assembler_A2) into Input
!insert(Assembler_A2, Tray_T7) into Output

!new Polisher('Polisher_P2')
!Polisher_P2.Location:=(x: 25, y: 20)
!Polisher_P2.processingTime:=5
!new Tray('Tray_T8')
!Tray_T8.capacity:=60
!insert(Tray_T7, Polisher_P2) into Input
!insert(Polisher_P2, Tray_T8) into Output

!repeat 50 times
!new Handle('Handle_H', Tray_T5) into Content

!repeat 50 times
!new Head('Head_H', Tray_T6) into Content

!repeat 45 times
!new Hammer('Hammer_H', Tray_T7) isPolished:=False into Content

!repeat 45 times
!new Hammer('Hammer_H', Tray_T8) isPolished:=True into Content
```

# Category: Unrealistc but valid
Develop instances that are syntactically and constraints-wise/multiplicity-wise valid but unlikely or impossible in real life. Edge cases and uncommon combinations of relationships and attributes.

Here are several instances of the AssemblyChain model, adhering to the constraints and multiplicities but featuring unlikely or impossible real-world scenarios:

### Instance 1: The Infinite Handle Loop

**Components:**

- **HandleGeneratorHG1**
  - Attributes:
    - `x: 10, y: 20, processingTime: 5, counter: 100`
  - Relationships:
    - Output to Tray T1

- **TrayT1**
  - Attributes:
    - `capacity: 1000`
  - Relationships:
    - Content: 1000 Handles (from HG1)
    - Inputs to Assembler A1 and HandleGenerator HG1

- **AssemblerA1**
  - Attributes:
    - `x: 15, y: 25, processingTime: 10`
  - Relationships:
    - Outputs to Tray T2

- **TrayT2**
  - Attributes:
    - `capacity: 100`
  - Relationships:
    - Content: 100 Hammers
    - Inputs to Polisher P1

- **PolisherP1**
  - Attributes:
    - `x: 20, y: 30, processingTime: 8`
  - Relationships:
    - Outputs to Tray T3

- **TrayT3**
  - Attributes:
    - `capacity: 50`
  - Content:
    - 50 Polished Hammers

**Description:**

- In this scenario, the HandleGenerator (HG1) illogically receives input from a tray full of Handles (T1), creating an impossible loop where handles are "generated" from existing handles. The assembler (A1) correctly uses handles from T1 and heads from another source (not shown here) to produce hammers, which are then polished.

### Instance 2: The Polisher's Dilemma

**Components:**

- **HeadGeneratorHG2**
  - Attributes:
    - `x: 5, y: 10, processingTime: 6, counter: 500`
  - Relationships:
    - Output to Tray T4

- **TrayT4**
  - Attributes:
    - `capacity: 500`
  - Content:
    - 500 Heads

- **AssemblerA2**
  - Attributes:
    - `x: 10, y: 15, processingTime: 10`
  - Relationships:
    - Inputs from Tray T4 (Heads) and another tray (Handles)
    - Outputs to Tray T5

- **TrayT5**
  - Attributes:
    - `capacity: 50`
  - Content:
    - 50 Hammers

- **PolisherP2**
  - Attributes:
    - `x: 15, y: 20, processingTime: 5`
  - Relationships:
    - Inputs from Tray T5 and Tray T6 (a tray with no hammers, violating the invariant)

- **TrayT6**
  - Attributes:
    - `capacity: 50`
  - Content:
    - 50 Handles (violates Polisher's input requirement)

**Description:**

- This instance features a polisher (P2) connected to a tray that contains handles instead of hammers, violating the invariant that polishers must receive trays containing only hammers. This presents a scenario where the polisher is incorrectly set up in the assembly line.

### Instance 3: The Overloaded Tray

**Components:**

- **HandleGeneratorHG3**
  - Attributes:
    - `x: 2, y: 3, processingTime: 4, counter: 1000`
  - Relationships:
    - Output to Tray T7

- **HeadGeneratorHG4**
  - Attributes:
    - `x: 3, y: 4, processingTime: 4, counter: 1000`
  - Relationships:
    - Output to Tray T7

- **TrayT7**
  - Attributes:
    - `capacity: 500`
  - Content:
    - 500 Handles and 500 Heads (violating invariant of same-type pieces)

- **AssemblerA3**
  - Attributes:
    - `x: 5, y: 6, processingTime: 10`
  - Relationships:
    - Inputs from Tray T7

- **TrayT8**
  - Attributes:
    - `capacity: 100`
  - Content:
    - 100 Hammers

**Description:**

- Tray T7 contains a mix of Handles and Heads, violating the invariant that all pieces in a tray must be of the same type. This scenario illustrates an overloaded and misconfigured tray feeding into an assembler (A3), which should not be able to process mixed content according to the model's rules.
```
```
!new HandleGenerator('hg1')
!hg1.x:=10
!hg1.y:=20
!hg1.processingTime:=5
!hg1.counter:=100

!new Tray('t1')
!t1.capacity:=1000

!new Assembler('a1')
!a1.x:=15
!a1.y:=25
!a1.processingTime:=10

!new Tray('t2')
!t2.capacity:=100

!new Polisher('p1')
!p1.x:=20
!p1.y:=30
!p1.processingTime:=8

!new Tray('t3')
!t3.capacity:=50

!insert(hg1,t1) into Output
!insert(t1,a1) into Input
!insert(a1,t2) into Output
!insert(t2,p1) into Input
!insert(p1,t3) into Output

!new HeadGenerator('hg2')
!hg2.x:=5
!hg2.y:=10
!hg2.processingTime:=6
!hg2.counter:=500

!new Tray('t4')
!t4.capacity:=500

!new Assembler('a2')
!a2.x:=10
!a2.y:=15
!a2.processingTime:=10

!new Tray('t5')
!t5.capacity:=50

!new Polisher('p2')
!p2.x:=15
!p2.y:=20
!p2.processingTime:=5

!new Tray('t6')
!t6.capacity:=50

!insert(hg2,t4) into Output
!insert(t4,a2) into Input
!insert(a2,t5) into Output
!insert(t5,p2) into Input
!insert(p2,t6) into Output

!new HandleGenerator('hg3')
!hg3.x:=2
!hg3.y:=3
!hg3.processingTime:=4
!hg3.counter:=1000

!new HeadGenerator('hg4')
!hg4.x:=3
!hg4.y:=4
!hg4.processingTime:=4
!hg4.counter:=1000

!new Tray('t7')
!t7.capacity:=500

!new Assembler('a3')
!a3.x:=5
!a3.y:=6
!a3.processingTime:=10

!new Tray('t8')
!t8.capacity:=100

!insert(hg3,t7) into Output
!insert(hg4,t7) into Output
!insert(t7,a3) into Input
!insert(a3,t8) into Output
```
```

# Category: Boundary Instances
Create instances that test boundary values of constraints, such as:
- Minimum and maximum multiplicities.
- Empty collections for optional associations.
- Extreme values for numeric or range invariants constraints.

### Instance 1: Minimum Multiplicities and Empty Collections

**HandleGenerator1**  
- Type: HandleGenerator  
- Attributes:  
  - `x`: 0  
  - `y`: 0  
  - `processingTime`: 5  
  - `counter`: 0  
- Output Association:  
  - `Tray`: **Tray1**  

**HeadGenerator1**  
- Type: HeadGenerator  
- Attributes:  
  - `x`: 5  
  - `y`: 0  
  - `processingTime`: 5  
  - `counter`: 0  
- Output Association:  
  - `Tray`: **Tray2**

**Tray1**  
- Type: Tray  
- Attributes:  
  - `capacity`: 10  
- Content Aggregation:  
  - Pieces: [] (empty collection, initially no handles generated)

**Tray2**  
- Type: Tray  
- Attributes:  
  - `capacity`: 10  
- Content Aggregation:  
  - Pieces: [] (empty collection, initially no heads generated)

- **Note**: This instance demonstrates minimum multiplicities with empty trays, ensuring no generated pieces initially.

### Instance 2: Maximum Multiplicities and Extreme Values

**HandleGenerator2**  
- Type: HandleGenerator  
- Attributes:  
  - `x`: 0  
  - `y`: 5  
  - `processingTime`: 10  
  - `counter`: 1000 (extreme value demonstrating high productivity)  
- Output Association:  
  - `Tray`: **Tray3**  

**HeadGenerator2**  
- Type: HeadGenerator  
- Attributes:  
  - `x`: 5  
  - `y`: 5  
  - `processingTime`: 10  
  - `counter`: 1000 (extreme value demonstrating high productivity)  
- Output Association:  
  - `Tray`: **Tray4**

**Tray3**  
- Type: Tray  
- Attributes:  
  - `capacity`: 100  
- Content Aggregation:  
  - Pieces: [Handle, Handle, Handle, ..., Handle] (100 handles)

**Tray4**  
- Type: Tray  
- Attributes:  
  - `capacity`: 100  
- Content Aggregation:  
  - Pieces: [Head, Head, Head, ..., Head] (100 heads)

- **Note**: This instance explores maximum multiplicities with trays at full capacity and an extreme counter value on generators.

### Instance 3: Assembler and Polisher with Full Cycle

**Assembler1**  
- Type: Assembler  
- Attributes:  
  - `x`: 10  
  - `y`: 5  
  - `processingTime`: 15  
- Input Association:  
  - Trays: **Tray3** (Handles), **Tray4** (Heads)  
- Output Association:  
  - `Tray`: **Tray5**

**Polisher1**  
- Type: Polisher  
- Attributes:  
  - `x`: 15  
  - `y`: 5  
  - `processingTime`: 5  
- Input Association:  
  - Tray: **Tray5**

**Tray5**  
- Type: Tray  
- Attributes:  
  - `capacity`: 50  
- Content Aggregation:  
  - Pieces: [Hammer, Hammer, ..., Hammer] (50 unpolished hammers)

- **Note**: This instance demonstrates a full cycle from assembly to polishing, ensuring proper tray content and order.

### Instance 4: Regional and Cultural Diversity

**HandleGenerator3**  
- Type: HandleGenerator  
- Attributes:  
  - `x`: 3  
  - `y`: 0  
  - `processingTime`: 8  
  - `counter`: 250  
- Output Association:  
  - `Tray`: **Tray6**  

**HeadGenerator3**  
- Type: HeadGenerator  
- Attributes:  
  - `x`: 8  
  - `y`: 0  
  - `processingTime`: 8  
  - `counter`: 250  
- Output Association:  
  - `Tray`: **Tray7**

**Tray6**  
- Type: Tray  
- Attributes:  
  - `capacity`: 75  
- Content Aggregation:  
  - Pieces: [Handle, Handle, Handle, ..., Handle] (75 handles)

**Tray7**  
- Type: Tray  
- Attributes:  
  - `capacity`: 75  
- Content Aggregation:  
  - Pieces: [Head, Head, Head, ..., Head] (75 heads)

**Assembler2**  
- Type: Assembler  
- Attributes:  
  - `x`: 12  
  - `y`: 0  
  - `processingTime`: 12  
- Input Association:  
  - Trays: **Tray6** (Handles), **Tray7** (Heads)  
- Output Association:  
  - `Tray`: **Tray8**

**Tray8**  
- Type: Tray  
- Attributes:  
  - `capacity`: 50  
- Content Aggregation:  
  - Pieces: [Hammer, Hammer, ..., Hammer] (50 unpolished hammers)

- **Note**: This instance reflects regional diversity with alternative handling of assembly line productivity and cultural naming conventions for machines.
```
!new HandleGenerator('HandleGenerator1')
!HandleGenerator1.x:=0
!HandleGenerator1.y:=0
!HandleGenerator1.processingTime:=5
!HandleGenerator1.counter:=0

!new HeadGenerator('HeadGenerator1')
!HeadGenerator1.x:=5
!HeadGenerator1.y:=0
!HeadGenerator1.processingTime:=5
!HeadGenerator1.counter:=0

!new Tray('Tray1')
!Tray1.capacity:=10

!new Tray('Tray2')
!Tray2.capacity:=10

!insert(HandleGenerator1,Tray1) into Output
!insert(HeadGenerator1,Tray2) into Output

!new HandleGenerator('HandleGenerator2')
!HandleGenerator2.x:=0
!HandleGenerator2.y:=5
!HandleGenerator2.processingTime:=10
!HandleGenerator2.counter:=1000

!new HeadGenerator('HeadGenerator2')
!HeadGenerator2.x:=5
!HeadGenerator2.y:=5
!HeadGenerator2.processingTime:=10
!HeadGenerator2.counter:=1000

!new Tray('Tray3')
!Tray3.capacity:=100

!new Tray('Tray4')
!Tray4.capacity:=100

!insert(HandleGenerator2,Tray3) into Output
!insert(HeadGenerator2,Tray4) into Output

!new Assembler('Assembler1')
!Assembler1.x:=10
!Assembler1.y:=5
!Assembler1.processingTime:=15

!new Tray('Tray5')
!Tray5.capacity:=50

!insert(Tray3,Assembler1) into Input
!insert(Tray4,Assembler1) into Input
!insert(Assembler1,Tray5) into Output

!new Polisher('Polisher1')
!Polisher1.x:=15
!Polisher1.y:=5
!Polisher1.processingTime:=5

!new Tray('Tray9')
!Tray9.capacity:=50

!insert(Tray5,Polisher1) into Input
!insert(Polisher1,Tray9) into Output

!new HandleGenerator('HandleGenerator3')
!HandleGenerator3.x:=3
!HandleGenerator3.y:=0
!HandleGenerator3.processingTime:=8
!HandleGenerator3.counter:=250

!new HeadGenerator('HeadGenerator3')
!HeadGenerator3.x:=8
!HeadGenerator3.y:=0
!HeadGenerator3.processingTime:=8
!HeadGenerator3.counter:=250

!new Tray('Tray6')
!Tray6.capacity:=75

!new Tray('Tray7')
!Tray7.capacity:=75

!insert(HandleGenerator3,Tray6) into Output
!insert(HeadGenerator3,Tray7) into Output

!new Assembler('Assembler2')
!Assembler2.x:=12
!Assembler2.y:=0
!Assembler2.processingTime:=12

!new Tray('Tray8')
!Tray8.capacity:=50

!insert(Tray6,Assembler2) into Input
!insert(Tray7,Assembler2) into Input
!insert(Assembler2,Tray8) into Output
```

# Category: Baseline Instances
Describe typical or standard instances that align with common expectations of the model. Ensure every class and relationship is represented at least once in a baseline configuration.

Below are complete and diverse instances for the AssemblyChain model, ensuring all classes, attributes, and relationships are represented in alignment with the specified constraints and invariants.

### Instance 1: Basic Hammer Assembly Line

#### Elements and Machines

1. **HandleGenerator1**
   - Inherits from: HandleGenerator
   - Attributes:
     - `x: 10`, `y: 20`, `processingTime: 5`, `counter: 100`
   - Relationships:
     - Outputs to: `Tray1`
   - Operations:
     - `start()`, `stop()`

2. **HeadGenerator1**
   - Inherits from: HeadGenerator
   - Attributes:
     - `x: 15`, `y: 25`, `processingTime: 6`, `counter: 100`
   - Relationships:
     - Outputs to: `Tray2`
   - Operations:
     - `start()`, `stop()`

3. **Assembler1**
   - Inherits from: Assembler
   - Attributes:
     - `x: 20`, `y: 30`, `processingTime: 10`
   - Relationships:
     - Inputs: `Tray1`, `Tray2`
     - Outputs to: `Tray3`
   - Operations:
     - `start()`, `stop()`

4. **Polisher1**
   - Inherits from: Polisher
   - Attributes:
     - `x: 25`, `y: 35`, `processingTime: 4`
   - Relationships:
     - Inputs: `Tray3`
     - Outputs to: `Tray4`
   - Operations:
     - `start()`, `stop()`

#### Trays

1. **Tray1**
   - Attributes:
     - `x: 12`, `y: 22`, `capacity: 50`
   - Contents: 
     - Pieces: 40 Handles (All of the same type)

2. **Tray2**
   - Attributes:
     - `x: 17`, `y: 27`, `capacity: 50`
   - Contents: 
     - Pieces: 40 Heads (All of the same type)

3. **Tray3**
   - Attributes:
     - `x: 22`, `y: 32`, `capacity: 30`
   - Contents: 
     - Pieces: 30 Hammers (All of the same type, not yet polished)

4. **Tray4**
   - Attributes:
     - `x: 27`, `y: 37`, `capacity: 30`
   - Contents: 
     - Pieces: 30 Hammers (All of the same type, polished)

#### Pieces

1. **Handle1 to Handle40**
   - Inherits from: Handle
   - Represents: Individual handles in `Tray1`

2. **Head1 to Head40**
   - Inherits from: Head
   - Represents: Individual heads in `Tray2`

3. **Hammer1 to Hammer30**
   - Inherits from: Hammer
   - Attributes:
     - `isPolished: False`
   - Represents: Individual hammers in `Tray3`

4. **Hammer31 to Hammer60**
   - Inherits from: Hammer
   - Attributes:
     - `isPolished: True`
   - Represents: Individual hammers in `Tray4`

### Instance 2: Advanced Hammer Assembly Line with Regional Variation (European Market)

#### Elements and Machines

1. **HandleGeneratorEU**
   - Inherits from: HandleGenerator
   - Attributes:
     - `x: 50`, `y: 60`, `processingTime: 7`, `counter: 200`
   - Relationships:
     - Outputs to: `TrayEU1`
   - Operations:
     - `start()`, `stop()`

2. **HeadGeneratorEU**
   - Inherits from: HeadGenerator
   - Attributes:
     - `x: 55`, `y: 65`, `processingTime: 8`, `counter: 200`
   - Relationships:
     - Outputs to: `TrayEU2`
   - Operations:
     - `start()`, `stop()`

3. **AssemblerEU**
   - Inherits from: Assembler
   - Attributes:
     - `x: 60`, `y: 70`, `processingTime: 12`
   - Relationships:
     - Inputs: `TrayEU1`, `TrayEU2`
     - Outputs to: `TrayEU3`
   - Operations:
     - `start()`, `stop()`

4. **PolisherEU**
   - Inherits from: Polisher
   - Attributes:
     - `x: 65`, `y: 75`, `processingTime: 5`
   - Relationships:
     - Inputs: `TrayEU3`
     - Outputs to: `TrayEU4`
   - Operations:
     - `start()`, `stop()`

#### Trays

1. **TrayEU1**
   - Attributes:
     - `x: 52`, `y: 62`, `capacity: 100`
   - Contents: 
     - Pieces: 80 Handles (All of the same type)

2. **TrayEU2**
   - Attributes:
     - `x: 57`, `y: 67`, `capacity: 100`
   - Contents: 
     - Pieces: 80 Heads (All of the same type)

3. **TrayEU3**
   - Attributes:
     - `x: 62`, `y: 72`, `capacity: 50`
   - Contents: 
     - Pieces: 50 Hammers (All of the same type, not yet polished)

4. **TrayEU4**
   - Attributes:
     - `x: 67`, `y: 77`, `capacity: 50`
   - Contents: 
     - Pieces: 50 Hammers (All of the same type, polished)

#### Pieces

1. **HandleEU1 to Handle80**
   - Inherits from: Handle
   - Represents: Individual handles in `TrayEU1`

2. **HeadEU1 to Head80**
   - Inherits from: Head
   - Represents: Individual heads in `TrayEU2`

3. **HammerEU1 to Hammer50**
   - Inherits from: Hammer
   - Attributes:
     - `isPolished: False`
   - Represents: Individual hammers in `TrayEU3`

4. **HammerEU51 to HammerEU100**
   - Inherits from: Hammer
   - Attributes:
     - `isPolished: True`
   - Represents: Individual hammers in `TrayEU4`

These instances provide a comprehensive view of the AssemblyChain model, illustrating different configurations and regional variations in the manufacturing process.
```
```
!new HandleGenerator('HandleGenerator1')
!HandleGenerator1.x:=10
!HandleGenerator1.y:=20
!HandleGenerator1.processingTime:=5
!HandleGenerator1.counter:=100

!new HeadGenerator('HeadGenerator1')
!HeadGenerator1.x:=15
!HeadGenerator1.y:=25
!HeadGenerator1.processingTime:=6
!HeadGenerator1.counter:=100

!new Assembler('Assembler1')
!Assembler1.x:=20
!Assembler1.y:=30
!Assembler1.processingTime:=10

!new Polisher('Polisher1')
!Polisher1.x:=25
!Polisher1.y:=35
!Polisher1.processingTime:=4

!new Tray('Tray1')
!Tray1.x:=12
!Tray1.y:=22
!Tray1.capacity:=50

!new Tray('Tray2')
!Tray2.x:=17
!Tray2.y:=27
!Tray2.capacity:=50

!new Tray('Tray3')
!Tray3.x:=22
!Tray3.y:=32
!Tray3.capacity:=30

!new Tray('Tray4')
!Tray4.x:=27
!Tray4.y:=37
!Tray4.capacity:=30

!insert(HandleGenerator1,Tray1) into Output
!insert(HeadGenerator1,Tray2) into Output
!insert(Tray1,Assembler1) into Input
!insert(Tray2,Assembler1) into Input
!insert(Assembler1,Tray3) into Output
!insert(Tray3,Polisher1) into Input
!insert(Polisher1,Tray4) into Output

!new HandleGenerator('HandleGeneratorEU')
!HandleGeneratorEU.x:=50
!HandleGeneratorEU.y:=60
!HandleGeneratorEU.processingTime:=7
!HandleGeneratorEU.counter:=200

!new HeadGenerator('HeadGeneratorEU')
!HeadGeneratorEU.x:=55
!HeadGeneratorEU.y:=65
!HeadGeneratorEU.processingTime:=8
!HeadGeneratorEU.counter:=200

!new Assembler('AssemblerEU')
!AssemblerEU.x:=60
!AssemblerEU.y:=70
!AssemblerEU.processingTime:=12

!new Polisher('PolisherEU')
!PolisherEU.x:=65
!PolisherEU.y:=75
!PolisherEU.processingTime:=5

!new Tray('TrayEU1')
!TrayEU1.x:=52
!TrayEU1.y:=62
!TrayEU1.capacity:=100

!new Tray('TrayEU2')
!TrayEU2.x:=57
!TrayEU2.y:=67
!TrayEU2.capacity:=100

!new Tray('TrayEU3')
!TrayEU3.x:=62
!TrayEU3.y:=72
!TrayEU3.capacity:=50

!new Tray('TrayEU4')
!TrayEU4.x:=67
!TrayEU4.y:=77
!TrayEU4.capacity:=50

!insert(HandleGeneratorEU,TrayEU1) into Output
!insert(HeadGeneratorEU,TrayEU2) into Output
!insert(TrayEU1,AssemblerEU) into Input
!insert(TrayEU2,AssemblerEU) into Input
!insert(AssemblerEU,TrayEU3) into Output
!insert(TrayEU3,PolisherEU) into Input
!insert(PolisherEU,TrayEU4) into Output
```
```