import cv2

import matplotlib.pyplot as plt

path_to_image = ""  # wird später gesetzt

shape_detection_tolerance = 0.02  # Mit diesem Wert dann rumspielen. 0.01 und 0.02 funktionieren ganz gut

# simuliert die Kamera, die ein Bild macht
def takeImage():
    return cv2.imread(r"C:\Users\olive\Documents\IntelliJ\RobotAPI\src\main\java\shapedetectionprogram\test.png")


# gibt den Mittelpunkt der Form zurück
def getCenterOfShape(approx):
    x, y, w, h = cv2.boundingRect(approx)
    x_mid = int(x + w / 2)
    y_mid = int(y + h / 2)
    return x_mid, y_mid

# def plotList(list_of_coords):
#     x_values = []
#     y_values = []
#
#     for coordinate in list_of_coords:
#         x_values.append(coordinate[1][0])
#         y_values.append(coordinate[1][1])
#
#         if coordinate[0] == 0:
#             shape = "Kreis"
#         elif coordinate[0] == 3:
#             shape = "Dreieck"
#         else:
#             shape = "Rechteck"
#
#         plt.annotate(shape, xy=(coordinate[1][0], coordinate[1][1]), xytext=(coordinate[1][0] + 10, coordinate[1][1] + 10))
#
#     plt.ylim(robot_working_space[1] + 100, -100)
#     plt.xlim(-100, robot_working_space[0] + 100)
#
#     x1, y1 = [0, robot_working_space[0]], [0, 0]
#     plt.plot(x1, y1)
#
#     x1, y1 = [0, robot_working_space[0]], [robot_working_space[1], robot_working_space[1]]
#     plt.plot(x1, y1)
#
#     x1, y1 = [robot_working_space[0], robot_working_space[0]], [robot_working_space[1], 0]
#     plt.plot(x1, y1)
#
#     x1, y1 = [0, 0], [0, robot_working_space[1]]
#     plt.plot(x1, y1)
#
#     plt.scatter(x_values, y_values)
#     plt.show()


def detectShapes():
    #load_properties()

    image = takeImage()

    gray_image = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)  # grau machen

    # _, thresh_image = cv2.threshold(gray_image, 220, 255, cv2.THRESH_BINARY) # farben klar definieren

    # 220 bzw 100 steht für einen Farbton. Bei allem darunter wird ausgeschwärzt
    _, thresh_image = cv2.threshold(gray_image, 150, 255,
                                    cv2.THRESH_BINARY)  # 100 funktioniert bei gradient besser als 220

    contours, _ = cv2.findContours(thresh_image, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)  # Formen finden

    original_image_dimensions = (image.shape[1], image.shape[0])

    print("Originale Masse des Bildes:" + str(original_image_dimensions))

    list_of_shapes = []

    for i, contour in enumerate(contours):
        if i == 0:  # Da ist wohl keins drin (vielleicht das ganze Bild)
            continue
        epsilon = shape_detection_tolerance * cv2.arcLength(contour, True)  # Toleranzen (Kanten glätten)
        approx = cv2.approxPolyDP(contour, epsilon, True)  # Ecken herausfinden
        cv2.drawContours(image, contour, 0, (0, 0, 0), 4)  # was tut das? xD

        coords = getCenterOfShape(approx)

        corners_of_shape = len(approx)

        if corners_of_shape == 3:
            shape_type = "triangle"
        elif corners_of_shape == 4:
            shape_type = "square"
        elif corners_of_shape == 8:  # stellt sicher, dass keine anderen Formen und als Kreis angezeigt gefunden werden
            shape_type = "circle"
        else:
            shape_type = "undefined"

        print(shape_type + ";" + str(coords[0]) + ";" + str(coords[1]))
    # plotList(list_of_shapes)
    return list_of_shapes

if __name__ == '__main__':
    shapes = detectShapes()