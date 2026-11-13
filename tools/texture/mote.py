import sys
from pathlib import Path

import numpy as np
from PIL import Image

ROOT = Path(__file__).resolve().parents[2]
VERSIONS = ['1.20.1', '1.21.1', '26.1.2', '26.2', '26.3']
PARTICLE_DIR = 'common/src/main/resources/assets/particular/textures/particle'

TONES = {
    '0': (255, 255, 255, 255),
    '1': (244, 244, 240, 255),
    '2': (232, 232, 226, 170),
    '3': (222, 222, 214, 64),
}

SIZE = 7
MOTES = [
    [
        '.22.',
        '2012',
        '2112',
        '.22.',
    ],
    [
        '323',
        '201',
        '312',
    ],
    [
        '01',
        '12',
    ],
    [
        '.333.',
        '32123',
        '31013',
        '32123',
        '.333.',
    ],
]


def sprite(rows):
    out = np.zeros((SIZE, SIZE, 4), np.uint8)
    h, w = len(rows), max(len(r) for r in rows)
    oy, ox = (SIZE - h) // 2, (SIZE - w) // 2
    for y, row in enumerate(rows):
        for x, key in enumerate(row):
            if key != '.':
                out[oy + y, ox + x] = TONES[key]
    return out


def build():
    return {f'mote_{i}.png': sprite(rows) for i, rows in enumerate(MOTES)}


def main():
    files = build()
    targets = [Path(sys.argv[1])] if len(sys.argv) > 1 else [ROOT / 'versions' / v / PARTICLE_DIR for v in VERSIONS]
    for target in targets:
        target.mkdir(parents=True, exist_ok=True)
        for name, img in files.items():
            Image.fromarray(img, 'RGBA').save(target / name)


if __name__ == '__main__':
    main()
